package com.gnirps.keycloak.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class CustomAuthenticationFailureHandler(
        @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
            request: HttpServletRequest,
            response: HttpServletResponse,
            exception: AuthenticationException
    ) {
        resolver.resolveException(request, response, null, exception)
    }
}