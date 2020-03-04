package com.gnirps.acme.controller

import com.gnirps.commons.logging.service.Logger
import org.shredzone.acme4j.exception.AcmeException
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler


@Configuration
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class AcmeExceptionHandlerController(
    private val logger: Logger
) {
    // TODO Handle each exception in a different way
    @ExceptionHandler(AcmeException::class)
    fun handleAcmeException(
        exception: AcmeException
    ): ResponseEntity<Any> {
        return logAndFormat(
            exception,
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    private fun logAndFormat(
        exception: Exception,
        status: HttpStatus
    ): ResponseEntity<Any> {
        logger.error(exception)
        return ResponseEntity(
            logger.formatMessage(exception),
            HttpHeaders(),
            status
        )
    }
}
