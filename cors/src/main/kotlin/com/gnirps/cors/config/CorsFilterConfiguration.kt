package com.gnirps.cors.config

import com.gnirps.cors.config.properties.CorsProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = [CorsProperties::class])
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
        response.setHeader("Access-Control-Allow-Credentials", corsProperties.accessControlAllowCredentials)
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
