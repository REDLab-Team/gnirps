package com.gnirps.swagger.config

import io.swagger.models.HttpMethod
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilterConfiguration : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
            servletRequest: ServletRequest,
            servletResponse: ServletResponse,
            filterChain: FilterChain
    ) {
        val response: HttpServletResponse = servletResponse as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        response.setHeader("Access-Control-Max-Age", "3600")
        if (HttpMethod.OPTIONS.name.equals(
                        (servletRequest as HttpServletRequest).method,
                        ignoreCase = true)
        ) {
            response.status = HttpServletResponse.SC_OK
        } else {
            filterChain.doFilter(servletRequest, servletResponse)
        }
    }

    override fun destroy() {}

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }
}
