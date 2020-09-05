package com.gnirps.keycloak.config

import com.gnirps.logging.service.Logger
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class CustomAccessDeniedHandler(private val logger: Logger) : AccessDeniedHandler {
    override fun handle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            exception: AccessDeniedException
    ) {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        logger.warn("user ${auth.name} attempted to access ${request.requestURI}")
        response.sendRedirect("${request.contextPath}/accessDenied");
    }
}