package com.gnirps.jwt.config

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class CustomAuthenticationManager: AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        val username: String = authentication.name
        val password: String = SecurityConfiguration.passwordEncoder.encode(authentication.credentials.toString())
        val grantedAuths: MutableList<GrantedAuthority> = ArrayList()
        grantedAuths.add(SimpleGrantedAuthority("ROLE_USER"))
        return UsernamePasswordAuthenticationToken(username, password, grantedAuths)
    }
}