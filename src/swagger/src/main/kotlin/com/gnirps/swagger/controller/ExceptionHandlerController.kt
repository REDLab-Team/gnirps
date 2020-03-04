package com.gnirps.swagger.controller

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
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException


@ControllerAdvice
class ExceptionHandlerController(private val logger: Logger) {

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception): ResponseEntity<Any> {
        return when (exception) {
            is JpaObjectRetrievalFailureException,
            is EntityNotFoundException -> {
                logAndFormat(exception, HttpStatus.NOT_FOUND)
            }
            is EntityExistsException -> {
                logAndFormat(exception, HttpStatus.SEE_OTHER)
            }
            is MissingServletRequestParameterException,
            is MethodArgumentNotValidException -> {
                logAndFormat(exception, HttpStatus.BAD_REQUEST)
            }
            is ResourceAccessException -> {
                logAndFormat(exception, HttpStatus.BAD_GATEWAY)
            }
            is AccessDeniedException -> {
                logAndFormat(exception, HttpStatus.UNAUTHORIZED)
            }
            is HttpException -> {
                logAndFormat(exception, exception.status)
            }
            else -> {
                logAndFormat(exception, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    private fun logAndFormat(
        exception: Exception,
        status: HttpStatus
    ): ResponseEntity<Any> {
        logger.error(exception)
        return ResponseEntity(
            logger.formatMessage(exception, Logger.EventType.ERROR),
            HttpHeaders(),
            status
        )
    }
}
