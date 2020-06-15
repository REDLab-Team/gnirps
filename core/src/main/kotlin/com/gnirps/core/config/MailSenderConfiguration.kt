package com.gnirps.core.config

import com.gnirps.core.config.properties.MailSenderProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailSenderConfiguration(private val mailSenderProperties: MailSenderProperties) {
    @Bean
    fun getJavaMailSender(): org.springframework.mail.javamail.JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailSenderProperties.host
        mailSender.port = mailSenderProperties.smtp.port.toInt()
        mailSender.username = mailSenderProperties.username
        mailSender.password = mailSenderProperties.password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = mailSenderProperties.transport.protocol
        if (mailSenderProperties.transport.protocol.equals("smtp")) {
            props["mail.smtp.auth"] = mailSenderProperties.smtp.auth
            props["mail.smtp.starttls.enable"] = mailSenderProperties.smtp.startTls.enable
        }
        props["mail.debug"] = mailSenderProperties.debug

        return mailSender
    }
}