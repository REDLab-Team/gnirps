package com.gnirps.swagger.controller

import com.gnirps.commons.logging.service.Logger
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.Exception

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component("LoggerInterceptorBean")
class LoggerInterceptor(private val logger: Logger): HandlerInterceptorAdapter() {
    companion object {
        val filteredURI: List<String> = listOf(
                "/oauth/check_token",
                "/webjars/springfox-swagger-ui/",
                "/swagger",
                "/error",
                "/csrf"
        )
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        exception: Exception?
    ) {
        filteredURI.forEach { if (request.requestURI.startsWith(it)) return }

        val content: String =
                        "method: ${request.method}, " +
                        "uri: ${request.requestURI}, " +
                        "status: ${response.status}"

        when (response.status) {
            in 400..499 -> logger.warn(content, Logger.EventType.HTTP_RESPONSE)
            in 500..599 -> exception
                    ?.fillInStackTrace()
                    ?.let { logger.printCleanStack(throwable = it) }
                    ?: logger.error(content, Logger.EventType.HTTP_RESPONSE)
            else -> logger.info(content, Logger.EventType.HTTP_RESPONSE)
        }
    }
}
