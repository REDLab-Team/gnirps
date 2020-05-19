package com.gnirps.swagger.config.properties

import com.gnirps.commons.config.properties.CustomPropertySources
import org.springframework.beans.factory.annotation.Value

@CustomPropertySources
class MailSenderProperties {
    @Value("\${gnirps.mail-sender.debug}")
    lateinit var debug: String

    @Value("\${gnirps.mail-sender.service-name}")
    lateinit var serviceName: String

    @Value("\${gnirps.mail-sender.host}")
    lateinit var host: String

    @Value("\${gnirps.mail-sender.username}")
    lateinit var username: String

    @Value("\${gnirps.mail-sender.password}")
    lateinit var password: String

    @Value("\${gnirps.mail-sender.recipients}")
    lateinit var recipients: List<String>

    @Value("\${gnirps.mail-sender.transport.protocol}")
    lateinit var transportProtocol: String

    @Value("\${gnirps.mail-sender.smtp.port}")
    lateinit var smtpPort: String

    @Value("\${gnirps.mail-sender.smtp.auth}")
    lateinit var smtpAuth: String

    @Value("\${gnirps.mail-sender.smtp.starttls.enable}")
    lateinit var smtpStartTlsEnable: String

    @Value("\${gnirps.mail-sender.smtp.starttls.required}")
    lateinit var smtpStartTlsRequired: String
}