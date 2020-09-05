package com.gnirps.core.config

import com.gnirps.core.config.properties.CorsProperties
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilterConfiguration(private val corsProperties: CorsProperties) : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
            servletRequest: ServletRequest,
            servletResponse: ServletResponse,
            filterChain: FilterChain
    ) {
        val response: HttpServletResponse = servletResponse as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", corsProperties.accessControlAllowOrigin)
        response.setHeader("Access-Control-Allow-Methods", corsProperties.accessControlAllowMethods)
        response.setHeader("Access-Control-Allow-Headers", corsProperties.accessControlAllowHeaders)
        response.setHeader("Access-Control-Max-Age", corsProperties.accessControlMaxAge)
        if (HttpMethod.OPTIONS.name.equals((servletRequest as HttpServletRequest).method, ignoreCase = true)) {
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
