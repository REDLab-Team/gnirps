package com.gnirps.core.controller

import com.gnirps.logging.service.Logger
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class LoggerInterceptor(private val logger: Logger) : HandlerInterceptor {
    companion object {
        // TODO Relocate to application.yml
        val filteredURI: List<String> = listOf(
                "/oauth/check_token",
                "/webjars/springfox-swagger-ui/",
                "/doc",
                "/v2",
                "/error",
                "/csrf"
        )
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // TODO Choose a strategy and clean the mess
//        val requestCacheWrapperObject: HttpServletRequest = ContentCachingRequestWrapper(request)
//        requestCacheWrapperObject.parameterMap
//        val inputString: String = requestCacheWrapperObject
//                .inputStream
//                .bufferedReader()
//                .use(BufferedReader::readText)
//                .let { if (it.isNotEmpty()) ",\ninputString: {$it}" else "" }
//        logger.debug(
//                "{" +
//                "\nmethod: ${request.method}" +
//                ",\nURI: ${request.requestURI}${getParameters(request)}" +
//                ",\nrequestClass: {${request.javaClass.simpleName}" +
//                ",\ncontentType: {${request.contentType}}" +
//                ",\ncontentSize: {${request.contentLength}}" +
//                inputString +
//                "\n}"
//        )
        return true
    }

    override fun afterCompletion(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any,
            exception: Exception?
    ) {
        filteredURI.forEach { if (request.requestURI.startsWith(it)) return }

        val content: String = "{" +
                "method: ${request.method}, " +
                "uri: ${request.requestURI}, " +
                "status: ${response.status}" +
                "}"

        when (response.status) {
            in 400..499 -> logger.warn(content, Logger.EventType.HTTP_RESPONSE)
            in 500..599 -> logger.error(content, Logger.EventType.HTTP_RESPONSE)
            else -> logger.info(content, Logger.EventType.HTTP_RESPONSE)
        }
    }

    private fun getParameters(request: HttpServletRequest): String {
        val posted = StringBuffer()
        val e: Enumeration<*>? = request.parameterNames
        if (e != null) {
            posted.append("?")
        }
        while (e!!.hasMoreElements()) {
            if (posted.length > 1) posted.append("&")
            val curr = e.nextElement() as String
            posted.append("$curr=")
            if (curr.contains("password") || curr.contains("pass") || curr.contains("pwd")) {
                posted.append("*****")
            } else {
                posted.append(request.getParameter(curr))
            }
        }
        val ip = request.getHeader("X-FORWARDED-FOR")
        val ipAddr: String = ip ?: getRemoteAddr(request)
        if (ipAddr != "") {
            posted.append("&_psip=$ipAddr")
        }
        return posted.toString()
    }

    private fun getRemoteAddr(request: HttpServletRequest): String {
        val ipFromHeader = request.getHeader("X-FORWARDED-FOR")
        if (ipFromHeader != null && ipFromHeader.isNotEmpty()) {
            return ipFromHeader
        }
        return request.remoteAddr ?: ""
    }
}
