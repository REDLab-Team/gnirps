package com.gnirps.swagger.controller

import com.gnirps.commons.exceptions.BashException
import com.gnirps.commons.exceptions.HttpException
import com.gnirps.commons.logging.service.Logger
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.util.NestedServletException
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException


@ControllerAdvice
class ExceptionHandlerController(private val logger: Logger) {

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception): ResponseEntity<Any> {
        val response = when (exception) {
            is HttpException                    -> logAndFormat(exception, exception.status)
            is JpaObjectRetrievalFailureException,
            is EntityNotFoundException          -> logAndFormat(exception, HttpStatus.NOT_FOUND)
            is EntityExistsException            -> logAndFormat(exception, HttpStatus.SEE_OTHER)
            is ConstraintViolationException,
            is MissingServletRequestParameterException,
            is MethodArgumentNotValidException  -> logAndFormat(exception, HttpStatus.BAD_REQUEST)
            is ResourceAccessException          -> logAndFormat(exception, HttpStatus.BAD_GATEWAY)
            is AccessDeniedException            -> logAndFormat(exception, HttpStatus.UNAUTHORIZED)
            is BashException                    -> logAndFormatBashException(exception)
            is NestedServletException           -> logAndFormatNestedException(exception)
            else                                -> logAndFormat(exception, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        logger.printCleanStack(exception)
        return response
    }

    private fun logAndFormat(exception: Exception, status: HttpStatus): ResponseEntity<Any> {
        logger.error(exception)
        return ResponseEntity(exception.localizedMessage, HttpHeaders(), status)
    }

    private fun logAndFormatBashException(exception: BashException): ResponseEntity<Any> {
        logger.error(exception)
        return if (exception.exitCode == 124) {
            ResponseEntity(exception.localizedMessage, HttpHeaders(), HttpStatus.REQUEST_TIMEOUT)
        } else {
            ResponseEntity(exception.localizedMessage, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun logAndFormatNestedException(exception: Exception): ResponseEntity<Any> {
        val token = "nested exception is "
        val rawMsg = exception.localizedMessage
        val nestedMsg = rawMsg.substring(rawMsg.indexOf(token) + token.length)
        val splitMsg = nestedMsg.split(":")
        val msg = when {
            splitMsg.size == 2 -> "{class: ${splitMsg[0]}, msg: ${splitMsg.drop(1).joinToString()}}"
            nestedMsg.isNotEmpty() -> nestedMsg
            else -> rawMsg
        }
        logger.error(msg)
        return ResponseEntity(msg, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
