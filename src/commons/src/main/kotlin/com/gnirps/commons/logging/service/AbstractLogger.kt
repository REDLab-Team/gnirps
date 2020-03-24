package com.gnirps.commons.logging.service

import com.gnirps.commons.exceptions.HttpException
import com.gnirps.commons.utils.isValidJson
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import java.io.BufferedReader
import java.sql.SQLException

abstract class AbstractLogger: Logger {
    companion object {
        fun formatMessage(content: Any? = null, eventType: Logger.EventType? = null): String {
            val formattedType: String = eventType?.name ?: inferType(content)
            val formattedContent: String = formatContent(content)
            return "{type: $formattedType, content: $formattedContent}"
        }

        private fun inferType(content: Any?): String {
            return when (content) {
                is Exception, is Error -> Logger.EventType.ERROR
                is ClientHttpResponse -> Logger.EventType.HTTP_RESPONSE
                is ClientHttpRequest -> Logger.EventType.HTTP_REQUEST
                else -> Logger.EventType.MISSING
            }.name
        }

        private fun formatContent(content: Any?): String {
           return when (content) {
                is HttpException -> {
                    "{" +
                            "class: ${content.javaClass.simpleName}, " +
                            "message: \"${content.localizedMessage}'\", " +
                            "status: ${content.status}" +
                    "}"
                }
                is MethodArgumentNotValidException -> {
                    "{" +
                            "class: ${content.javaClass.simpleName}, " +
                            "message: \"${content.bindingResult.fieldErrors[0].defaultMessage}\"" +
                    "}"
                }
                is ClientHttpResponse -> {
                    "{" +
                            "code: ${content.statusCode}, " +
                            "status: ${content.statusText}, " +
                            "body: \"" + content
                                    .body
                                    .bufferedReader()
                                    .use(BufferedReader::readText) +
                            "\"" +
                    "}"
                }
                is Exception -> {
                    "{" +
                            "class: ${content.javaClass.simpleName}, " +
                            "message: \"${content.localizedMessage}'\"" +
                    "}"
                }
                is String -> "{$content}"
                null -> "null"
                else -> "$content"
            }
        }
    }

    override fun cleanError(throwable: Throwable) {
        rootCause(throwable).let {
            it.stackTrace = ourCodeOnly(it).toTypedArray()
            error(it)
        }
    }

    override fun formatMessage(content: Any?, eventType: Logger.EventType): String {
        return AbstractLogger.formatMessage(content, eventType)
    }

    private fun ourCodeOnly(t: Throwable): List<StackTraceElement> {
        var enteredOurCode = false
        return t.stackTrace.filter {
            if (it.className.startsWith("com.gnirps")) {
                if (!enteredOurCode) {
                    enteredOurCode = true
                }
                true
            }
            else !enteredOurCode
        }
    }

    private fun rootCause(t: Throwable): Throwable {
        var current = t
        var cause: Throwable? = null
        while (current !== cause) {
            cause = current
            val nextCause = current.cause
            if (nextCause !== null) {
                current = nextCause
            }
        }
        return if (cause is SQLException)
            unwrapSqlException(cause)
        else cause
    }

    private fun unwrapSqlException(sqlException: SQLException): Throwable {
        val fullMessage = StringBuilder()
        var next: SQLException? = sqlException
        var cause: SQLException? = null
        var count = 1
        while (next !== null && next !== cause) {
            if (next !== sqlException) {
                fullMessage.append("\n")
            }
            fullMessage.append(count).append(") ").append(next.message)
            count++
            cause = next
            next = next.nextException

        }
        val exception = SQLException(fullMessage.toString())
        // retain original stack trace
        exception.stackTrace = sqlException.stackTrace
        return exception
    }
}