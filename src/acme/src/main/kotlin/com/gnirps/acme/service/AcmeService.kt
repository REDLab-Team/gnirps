package com.gnirps.acme.service

import org.shredzone.acme4j.util.KeyPairUtils
import com.gnirps.acme.config.properties.AcmeProperties
import com.gnirps.commons.logging.service.Logger
import org.shredzone.acme4j.*
import org.springframework.stereotype.Component
import java.io.*
import java.security.KeyPair
import org.shredzone.acme4j.AccountBuilder
import java.io.FileWriter
import org.shredzone.acme4j.Login
import java.net.URL
import java.security.KeyStore
import java.util.*
import javax.annotation.PostConstruct


@Component
class AcmeService(
    private val properties: AcmeProperties,
    private val orderManager: OrderManager,
    private val challengeStore: ChallengeStore,
    private val logger: Logger
) {
    companion object {
        private const val KEY_SIZE = 2048
    }

    private val session: Session =
            createSession()
    private val userKeyPair: KeyPair =
            getOrCreateKeyPair(properties.userKeyFile)
    private val domainKeyPair: KeyPair =
            getOrCreateKeyPair(properties.domainKeyFile)
    private val account: Account =
            getOrCreateAccount().account

    fun generateCertificate() {
        // if a keystore already exists, no need to fret
        if (!File(properties.keyStoreFile).exists()) {
            logger.info("keystore file not found, creating one", Logger.EventType.OPERATION)

            val order: Order = orderManager.createOrder(
                account = account,
                domains = mutableListOf(properties.domainName)
            )
            logger.info("order created")

            orderManager.processAuthorization(
                order = order,
                challengeStore = challengeStore
            )
            logger.info("authorization processed")

            orderManager.signCertificate(
                order = order,
                domains = mutableListOf(properties.domainName),
                organisation = properties.organisation,
                keyPair = domainKeyPair
            )
            logger.info("certificate signed")

            val certificate: Certificate = orderManager.getCertificate(order)
            orderManager.createKeyStore(
                certificate = certificate,
                alias = properties.organisation,
                fileName = properties.keyStoreFile,
                password = properties.keyStorePassword
            )

            System.setProperty(
                "javax.net.ssl.keyStore",
                properties.keyStoreFile
            )
            System.setProperty(
                "javax.net.ssl.keyStorePassword",
                properties.keyStorePassword
            )
        } else {
            logger.info("keystore file found at '${properties.keyStoreFile}'")
        }
    }

    fun getAuthorization(token: String): String {
        return challengeStore.get(token)
    }

    private final fun createSession(): Session {
        val environment: String = when (properties.environment) {
            "staging", "dev", "test" -> "staging"
            "production", "prod" -> ""
            else -> "error"
        }
        logger.info("creating session: 'acme://letsencrypt.org/$environment'", Logger.EventType.OPERATION)
        return Session("acme://letsencrypt.org/$environment")
    }

    private final fun getOrCreateAccount(
        keyPair: KeyPair = this.userKeyPair,
        session: Session = this.session,
        accountLocationUrl: String? = null
    ): Login {
        // get already existing account
        if (accountLocationUrl != null) {
            logger.info(
                "retrieving already existing account '$accountLocationUrl'"
            )
            return session.login(URL(accountLocationUrl), keyPair)
        }

        // create new account
        logger.info("creating new account", Logger.EventType.OPERATION)
        return AccountBuilder()
                .addContact("mailto:${properties.contactMail}")
                .agreeToTermsOfService()
                .useKeyPair(keyPair)
                .createLogin(session)

        // If the CA changes the terms of service and requires an explicit
        // agreement to the new terms, an AcmeUserActionRequiredException is
        // thrown. Its getInstance() method returns the URL of a document that
        // gives instructions about how to agree to the new terms of service.
        // There is no way to automatize this process.
    }

    private final fun getOrCreateKeyPair(fileName: String): KeyPair {
        return try { // key already exists
            val keyPair: KeyPair = readPrivateKey(fileName)
            logger.info("KeyPair read from file '$fileName'")
            keyPair
        } catch (exception: IOException) { // key does not exist
            val keyPair: KeyPair = KeyPairUtils.createKeyPair(KEY_SIZE)
            savePrivateKey(keyPair, fileName)
            logger.info("KeyPair created and stored in file '$fileName'", Logger.EventType.OPERATION)
            keyPair
        }
    }

    private fun savePrivateKey(keyPair: KeyPair, fileName: String) {
        FileWriter(fileName).use { KeyPairUtils.writeKeyPair(keyPair, it) }
    }

    private fun readPrivateKey(fileName: String): KeyPair {
        return FileReader(fileName).use { KeyPairUtils.readKeyPair(it) }
    }
}
