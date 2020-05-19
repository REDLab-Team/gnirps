package com.gnirps.swagger.config

import com.gnirps.swagger.config.properties.MailSenderProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class JavaMailSender(private val mailSenderProperties: MailSenderProperties) {
    @Bean
    fun getJavaMailSender(): org.springframework.mail.javamail.JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailSenderProperties.host
        mailSender.port = mailSenderProperties.smtpPort.toInt()
        mailSender.username = mailSenderProperties.username
        mailSender.password = mailSenderProperties.password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = mailSenderProperties.smtpAuth
        props["mail.smtp.starttls.enable"] = mailSenderProperties.smtpStartTlsEnable
        props["mail.debug"] = mailSenderProperties.debug

        return mailSender
    }
}