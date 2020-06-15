package com.gnirps.acme.service

import com.gnirps.logging.service.Logger
import org.shredzone.acme4j.*
import java.time.Instant
import org.shredzone.acme4j.challenge.Http01Challenge
import org.shredzone.acme4j.exception.AcmeException
import org.shredzone.acme4j.util.CSRBuilder
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.security.KeyPair
import java.security.KeyStore

// https://shredzone.org/maven/acme4j/usage/order.html
@Component
class OrderManager(private val logger: Logger) {
    companion object {
        private const val DEFAULT_KEYSTORE_TYPE: String = "pkcs12"
        private const val DEFAULT_ALIAS: String = "gnirps"
    }

    fun createOrder(
        account: Account,
        domains: MutableCollection<String>,
        notBefore: Instant? = null,
        notAfter: Instant? = null
    ): Order {
        var orderBuilder: OrderBuilder = account.newOrder().domains(domains)
        if (notBefore != null) orderBuilder = orderBuilder.notBefore(notBefore)
        if (notAfter != null) orderBuilder = orderBuilder.notAfter(notAfter)
        logger.info("creating order")
        return orderBuilder.create()
    }

    fun processAuthorization(order: Order, challengeStore: ChallengeStore) {
        order.authorizations.stream().filter { it.status !== Status
                .VALID }
                .forEach { auth ->
                    auth.findChallenge<Http01Challenge>(
                        Http01Challenge.TYPE
                    )?.let { challenge ->
                        logger.info(
                            "Storing challenge (token: " +
                                    "'${challenge.token}', authorization: " +
                                    "'${challenge.authorization}')",
                                Logger.EventType.OPERATION
                        )
                        challengeStore.put(
                            challenge.token,
                            challenge.authorization
                        )
                        logger.info("triggering challenge")
                        challenge.trigger()
                        val maxAttempts = 3
                        var attempt = 0
                        while(
                                auth.status != Status.VALID &&
                                attempt++ < maxAttempts
                        ) {
                            Thread.sleep(3000L)
                            logger.info(
                                "updating authorization... (attempt " +
                                        "[$attempt/$maxAttempts])"
                            )
                            auth.update()
                        }
                        if (auth.status != Status.VALID) {
                            throw AcmeException(
                                "authorization failed: ${challenge.json}"
                            )
                        }
                    }
                }
    }

    fun signCertificate(
        order: Order,
        domains: MutableCollection<String>,
        organisation: String,
        keyPair: KeyPair,
        fileName: String = "domain.csr"
    ) {
        File(fileName).let {
            if (it.exists()) {
                logger.info("executing order read from file '$fileName'", Logger.EventType.OPERATION)
                order.execute(it.readBytes())
            } else {
                logger.info("signing order...", Logger.EventType.OPERATION)
                val csrBuilder = CSRBuilder()
                csrBuilder.addDomains(domains)
                csrBuilder.setOrganization(organisation)
                csrBuilder.sign(keyPair)
                val csr: ByteArray = csrBuilder.encoded
                logger.info("saving order to file '$fileName'", Logger.EventType.OPERATION)
                it.writeBytes(csr)
                logger.info("executing order", Logger.EventType.OPERATION)
                order.execute(csr)
            }
        }
    }

    fun getCertificate(
        order: Order,
        fileName: String = "domain.crt"
    ): Certificate {
        order.update()
        val maxAttempts = 10
        var attempt = 0
        while (order.status != Status.VALID && attempt++ < maxAttempts) {
            if (order.status == Status.INVALID) {
                throw AcmeException("order failed: ${order.json}")
            }
            Thread.sleep(3000L)
            logger.info(
                "sending challenge request to provider: " +
                        "attempt " +
                        "[$attempt/$maxAttempts]",
                    Logger.EventType.OPERATION
            )
            order.update()
        }

        val certificate: Certificate = order.certificate ?: throw AcmeException(
            "could not obtain certificate, status: ${order.status}"
        )

        certificate.writeCertificate(FileWriter(fileName))
        return certificate
    }

    fun createKeyStore(
        certificate: Certificate,
        alias: String = DEFAULT_ALIAS,
        fileName: String = "keystore.jks",
        password: String
    ) {
        // create empty keystore
        val keyStore: KeyStore = KeyStore.getInstance(DEFAULT_KEYSTORE_TYPE)
        keyStore.load(null, password.toCharArray())

        // add certificate entry
        keyStore.setCertificateEntry(alias, certificate.certificate)

        // save to file
        keyStore.store(File(fileName).outputStream(), password.toCharArray())
    }
}
