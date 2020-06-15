package com.gnirps.swagger.controller

import com.gnirps.logging.exceptions.BashException
import com.gnirps.logging.exceptions.HttpException
import com.gnirps.logging.service.Logger
import com.gnirps.core.config.properties.MailSenderProperties
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.util.NestedServletException
import java.lang.reflect.UndeclaredThrowableException
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionHandlerController(
        private val logger: Logger,
        private val javaMailSender: JavaMailSender,
        private val mailSenderProperties: MailSenderProperties
) {
    private final val internalServerErrorMessage =
            "The maintainer has been notified of your error and shall intervene promptly. Hopefully. Maybe."

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Throwable): ResponseEntity<Any> {
        when (exception) {
            is UndeclaredThrowableException -> {
                logger.debug(exception, Logger.EventType.ERROR)
                return handleAll(exception.undeclaredThrowable)
            }
            is NestedServletException -> logger.error(extractNestServletExceptionMessage(exception))
            else -> logger.error(exception)
        }
        logger.printCleanStack(exception)

        val status: HttpStatus = getStatus(exception)
        val message: String = when (status) {
            HttpStatus.INTERNAL_SERVER_ERROR -> internalServerErrorMessage
            else -> exception.localizedMessage
        }.lines().joinToString(" ")

        notifyMaintainerByMail(exception, status)
        return ResponseEntity(message, HttpHeaders(), status)
    }

    private fun notifyMaintainerByMail(exception: Throwable, status: HttpStatus) {
        try {
            SimpleMailMessage().let {
                it.setTo(*mailSenderProperties.recipients.toTypedArray())
                it.subject = "[${mailSenderProperties.serviceName}]: $status"
                it.text = "[${exception.javaClass.simpleName}] ${exception.localizedMessage}" +
                        "\n\n" + "_".repeat(25) + "\n\n" +
                        logger.getCleanStack(exception) +
                        "\n\n" + "_".repeat(25) + "\n\n" +
                        "good luck, sir"
                javaMailSender.send(it)
            }
        } catch (mailException: MailException) {
            logger.error(mailException)
            logger.printCleanStack(mailException)
        }
    }

    private fun getStatus(exception: Throwable): HttpStatus {
        return when (exception) {
            is HttpException                   -> exception.status
            is JpaObjectRetrievalFailureException,
            is EntityNotFoundException         -> HttpStatus.NOT_FOUND
            is EntityExistsException           -> HttpStatus.SEE_OTHER
            is ConstraintViolationException,
            is MissingServletRequestParameterException,
            is MethodArgumentNotValidException -> HttpStatus.BAD_REQUEST
            is ResourceAccessException         -> HttpStatus.BAD_GATEWAY
            is AccessDeniedException           -> HttpStatus.UNAUTHORIZED
            is BashException ->
                when (exception.exitCode) {
                    124                        -> HttpStatus.GATEWAY_TIMEOUT
                    else                       -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            else                               -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    private fun extractNestServletExceptionMessage(exception: NestedServletException): String {
        val token = "nested exception is:"
        val rawMsg = exception.localizedMessage.lines().joinToString(" ")
        val nestedMsg = rawMsg.substring(rawMsg.indexOf(token) + token.length)
        val splitMsg = nestedMsg.trim().split(":")
        return when {
            splitMsg.size == 2 -> "{class: ${splitMsg[0]}, msg: ${splitMsg.drop(1).joinToString()}}"
            nestedMsg.isNotEmpty() -> nestedMsg
            else -> rawMsg
        }
    }
}
