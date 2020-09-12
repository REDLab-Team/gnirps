package com.gnirps.core.controller

import com.gnirps.core.config.properties.MailSenderProperties
import com.gnirps.logging.exceptions.BashException
import com.gnirps.logging.exceptions.HttpException
import com.gnirps.logging.service.Logger
import org.apache.http.client.ClientProtocolException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.util.NestedServletException
import java.lang.reflect.UndeclaredThrowableException
import java.util.concurrent.TimeoutException
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException
import javax.ws.rs.BadRequestException

// TODO consider defining specific ExceptionHandler methods for all cases
//      to allow overriding through other ControllerAdvices with higher
//      priority order, or not: it does mean to "consider" it.

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class ExceptionHandlerController(
        private val log: Logger,
        private val javaMailSender: JavaMailSender,
        private val mailSenderProperties: MailSenderProperties
) : ResponseEntityExceptionHandler() {
    private final val internalServerErrorMessage =
            "The maintainer has been notified of your error and shall intervene promptly. Hopefully. Maybe."

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Throwable): ResponseEntity<Any> {
        when (exception) {
            is UndeclaredThrowableException -> {
                log.debug(exception, Logger.EventType.ERROR)
                return handleAll(exception.undeclaredThrowable)
            }
            is NestedServletException -> log.error(extractNestServletExceptionMessage(exception))
            else -> log.error(exception)
        }

        val status: HttpStatus = getStatus(exception)
        if (status.is5xxServerError) log.printCleanStack(exception)

        val message: String = when (status) {
            HttpStatus.INTERNAL_SERVER_ERROR -> internalServerErrorMessage
            else -> exception.localizedMessage
        }.lines().joinToString(" ")

        notifyMaintainerByMail(exception, status)
        return ResponseEntity(message, HttpHeaders(), status)
    }

    private fun notifyMaintainerByMail(exception: Throwable, status: HttpStatus) {
        if (mailSenderProperties.recipients.isEmpty() || status.value() < 500) {
            return
        }
        try {
            SimpleMailMessage().let {
                it.setTo(*mailSenderProperties.recipients.toTypedArray())
                it.subject = "[${mailSenderProperties.serviceName}]: $status"
                it.text = "[${exception.javaClass.simpleName}] ${exception.localizedMessage}" +
                        "\n\n" + "_".repeat(25) + "\n\n" +
                        log.getCleanStack(exception) +
                        "\n\n" + "_".repeat(25) + "\n\n" +
                        "good luck, sir"
                javaMailSender.send(it)
            }
        } catch (mailException: MailException) {
            log.error(mailException)
            log.printCleanStack(mailException)
        }
    }

    fun getStatus(exception: Throwable): HttpStatus {
        return when (exception) {
            is HttpException -> exception.status
            is JpaObjectRetrievalFailureException,
            is EntityNotFoundException -> HttpStatus.NOT_FOUND
            is EntityExistsException -> HttpStatus.SEE_OTHER
            is ConstraintViolationException,
            is MissingServletRequestParameterException,
            is MethodArgumentNotValidException,
            is BadRequestException -> HttpStatus.BAD_REQUEST
            is ClientProtocolException -> HttpStatus.BAD_GATEWAY
            is ResourceAccessException -> HttpStatus.BAD_GATEWAY
            is AccessDeniedException -> HttpStatus.FORBIDDEN
            is TimeoutException -> HttpStatus.REQUEST_TIMEOUT
            is BashException ->
                when (exception.exitCode) {
                    124 -> HttpStatus.GATEWAY_TIMEOUT
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            else -> HttpStatus.INTERNAL_SERVER_ERROR
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
