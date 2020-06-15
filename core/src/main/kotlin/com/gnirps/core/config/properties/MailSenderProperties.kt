package com.gnirps.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.core.mail-sender")
data class MailSenderProperties(
        val debug: String,
        val serviceName: String,
        val host: String,
        val username: String,
        val password: String,
        val recipients: List<String>,
        val transport: Transport,
        val smtp: Smtp
) {
    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.core.mail-sender.transport")
    data class Transport(val protocol: String)

    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.core.mail-sender.smtp")
    data class Smtp(
            val port: String,
            val auth: String,
            val startTls: StartTls
    )

    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.core.mail-sender.smtp.starttls")
    data class StartTls(
            val enable: String,
            val required: String
    )
}