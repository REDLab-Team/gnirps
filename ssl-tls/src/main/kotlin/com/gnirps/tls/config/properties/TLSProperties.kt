package com.gnirps.tls.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.ssl")
data class TLSProperties(
        val port: Int,
        val keyStore: KeyStore,
        val trustStore: TrustStore
) {
    data class KeyStore(
            val type: String,
            val path: String,
            val password: String
    )
    data class TrustStore(
            val type: String? = null,
            val path: String? = null,
            val password: String? = null
    )
}