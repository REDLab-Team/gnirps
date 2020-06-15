package com.gnirps.logging.service

import com.gnirps.logging.exceptions.BashException
import com.gnirps.logging.exceptions.HttpException
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import java.io.BufferedReader
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.SQLException


abstract class AbstractLogger : Logger {
    override fun formatMessage(content: Any?, eventType: Logger.EventType?): String {
        val formattedType: String = eventType?.name ?: inferType(content)
        val formattedContent: String = formatContent(content)
        return "{\"type\": $formattedType, \"content\": $formattedContent}"
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
            is BashException -> {
                "{" +
                        "\"class\": \"${content.javaClass.simpleName}\", " +
                        "\"message\": \"${content.localizedMessage}\", " +
                        "\"exitCode\": \"${content.exitCode}\"" +
                        "}"
            }
            is HttpException -> {
                "{" +
                        "\"class\": \"${content.javaClass.simpleName}\", " +
                        "\"message\": \"${content.localizedMessage}\", " +
                        "\"status\": \"${content.status}\"" +
                        "}"
            }
            is MethodArgumentNotValidException -> {
                "{" +
                        "\"class\": \"${content.javaClass.simpleName}\", " +
                        "\"message\": \"${content.bindingResult.fieldErrors[0].defaultMessage}\"" +
                        "}"
            }
            is ClientHttpResponse -> {
                "{" +
                        "\"code\": ${content.statusCode}, " +
                        "\"status\": \"${content.statusText}\", " +
                        "\"body\": \"" + content
                        .body
                        .bufferedReader()
                        .use(BufferedReader::readText) +
                        "\"" +
                        "}"
            }
            is Exception -> {
                "{" +
                        "\"class\": \"${content.javaClass.simpleName}\", " +
                        "\"message\": \"${content.localizedMessage}\"" +
                        "}"
            }
            null -> "null"
            else -> "$content"
        }
    }

    override fun getCleanStack(throwable: Throwable): String {
        try {
            StringWriter().use { sw ->
                PrintWriter(sw).use { pw ->
                    ourCodeOnly(rootCause(throwable)).printStackTrace(pw)
                    return sw.toString()
                }
            }
        } catch (ioe: IOException) {
            throw IllegalStateException(ioe)
        }
    }

    override fun printCleanStack(throwable: Throwable) {
        error(content = "{${getCleanStack(throwable)}}", eventType = Logger.EventType.STACK_TRACE)
    }

    private fun ourCodeOnly(t: Throwable, prefix: String = "com.gnirps"): Throwable {
        var enteredOurCode = false
        t.stackTrace = t.stackTrace.filter {
            if (it.className.startsWith(prefix)) {
                if (!enteredOurCode) enteredOurCode = true
                true
            } else !enteredOurCode
        }.toTypedArray()
        return t
    }

    private fun rootCause(t: Throwable): Throwable {
        var current = t
        var cause: Throwable? = null
        while (current !== cause) {
            cause = current
            val nextCause = current.cause
            if (nextCause !== null) current = nextCause
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