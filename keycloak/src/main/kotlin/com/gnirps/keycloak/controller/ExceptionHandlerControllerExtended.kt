package com.gnirps.keycloak.controller

import com.gnirps.core.config.properties.MailSenderProperties
import com.gnirps.core.controller.ExceptionHandlerController
import com.gnirps.logging.service.Logger
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ExceptionHandlerControllerExtended(
        logger: Logger,
        javaMailSender: JavaMailSender,
        mailSenderProperties: MailSenderProperties
) : ExceptionHandlerController(logger, javaMailSender, mailSenderProperties) {
    @ExceptionHandler(Exception::class)
    override fun handleAll(exception: Throwable): ResponseEntity<Any> {
        return super.handleAll(exception)
    }

    override fun getStatus(exception: Throwable): HttpStatus {
        return when (exception) {
            is KeycloakAuthenticationException,
            is AuthenticationException,
            is HttpClientErrorException.Unauthorized -> HttpStatus.UNAUTHORIZED
            is AccessDeniedException -> HttpStatus.FORBIDDEN
            else -> super.getStatus(exception)
        }
    }
}