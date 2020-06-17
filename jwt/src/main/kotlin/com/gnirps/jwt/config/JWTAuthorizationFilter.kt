package com.gnirps.jwt.config

import com.gnirps.logging.config.defaultLogger
import com.gnirps.logging.service.Logger
import com.gnirps.jwt.util.TokenManager
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Authorization filter that allows to limit access to the various services provided by the API.
 * When receiving a user request, the filter will compare his access right (his role)
 * with the one stored in the database. If the user is authorized to access the requested service
 * the filter will let him pass. If not, an error will be sent to the user and a LOGGER message will be displayed.
 */
class JWTAuthorizationFilter(
        authManager : AuthenticationManager,
        private val tokenManager: TokenManager,
        private val userService: UserDetailsService
) : BasicAuthenticationFilter(authManager) {
    companion object {
        private val LOGGER: Logger = defaultLogger()
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader(SecurityConstants.HEADER_STRING)
        var username: String? = null
        val authToken: String
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            LOGGER.warn("couldn't find bearer string, will ignore the header")
            chain.doFilter(req, res)
            return
        } else {
            authToken = header.replace(SecurityConstants.TOKEN_PREFIX, "")
            try {
                username = tokenManager.getUsernameFromToken(authToken)
            } catch (iae: IllegalArgumentException) {
                LOGGER.error(iae)
            } catch (eje: ExpiredJwtException) {
                LOGGER.warn(eje)
            } catch (se: SignatureException) {
                LOGGER.error("authentication Failed. Username or Password not valid.")
            }
        }
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                val userDetails: UserDetails = userService.loadUserByUsername(username)
                if (tokenManager.validateToken(authToken, userDetails)) {
                    val authentication: UsernamePasswordAuthenticationToken = tokenManager.getAuthentication(
                            authToken, SecurityContextHolder.getContext().authentication, userDetails
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(req)
                    LOGGER.debug("user $username authenticated, setting security context...")
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (unfe: UsernameNotFoundException) {
                LOGGER.warn("email $username extracted from jwt token has no match in db")
            }
        }
        chain.doFilter(req, res)
    }
}