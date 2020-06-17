package com.gnirps.jwt.util

import com.gnirps.jwt.config.SecurityConstants
import com.gnirps.jwt.config.properties.JWTProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Provides the various functions associated with the token.
 */
@Service
class TokenManager(private val jwtProperties: JWTProperties) {

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token, Function { obj: Claims -> obj.subject })
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { obj: Claims -> obj.expiration })
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
            return Jwts.parser()
                .setSigningKey(jwtProperties.privateKey)
                .parseClaimsJws(token)
                .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(","))
        return Jwts.builder()
                .setSubject(authentication.name)
                .claim(SecurityConstants.AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.privateKey)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_IN_SECOND * 1000))
                .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun getAuthentication(
            token: String, existingAuth: Authentication, userDetails: UserDetails
    ): UsernamePasswordAuthenticationToken {
        val claims: Claims = Jwts
                .parser()
                .setSigningKey(jwtProperties.privateKey)
                .parseClaimsJws(token)
                .body
        val authorities: Collection<GrantedAuthority> = Arrays
                .stream(claims[SecurityConstants.AUTHORITIES_KEY].toString().split(",".toRegex()).toTypedArray())
                .map { role: String -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
    }
}