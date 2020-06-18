package com.gnirps.jwt.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.gnirps.jwt.dto.LoginRequest
import com.gnirps.jwt.util.TokenManager
import com.gnirps.logging.config.defaultLogger
import com.gnirps.logging.service.Logger
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Authentication filter that limits access to the API.
 * When an authentication request is received, the filter will compare the data received
 * with the data stored in the database. If the access requester exists,
 * an authentication token will be sent back to him.
 */
class JWTAuthenticationFilter(
        authManager: AuthenticationManager,
        private val tokenManager: TokenManager
) : UsernamePasswordAuthenticationFilter() {
    companion object {
        private val LOGGER: Logger = defaultLogger()
    }

    init {
        authenticationManager = authManager
    }

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication {
        val credentials: LoginRequest = ObjectMapper().readValue(req.inputStream, LoginRequest::class.java)
        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        credentials.username,
                        credentials.password,
                        emptyList<GrantedAuthority>()
                )
        )
    }

    override fun successfulAuthentication(
            req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain, auth: Authentication
    ) {
        LOGGER.debug(auth.name + " successfully sent a login request")
        val token: String = tokenManager.generateToken(auth)
        LOGGER.debug("generated token for user ${tokenManager.getUsernameFromToken(token)}: $token")
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token)
    }
}