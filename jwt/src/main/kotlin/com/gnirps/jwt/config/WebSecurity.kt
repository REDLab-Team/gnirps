package com.gnirps.jwt.config

import com.gnirps.jwt.config.properties.JWTProperties
import com.gnirps.jwt.util.TokenManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Implements a security layer on incoming http requests by determining the protected routes and filters to be applied.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
@ComponentScan("com.gnirps.jwt")
class WebSecurity(
        private val jwtProperties: JWTProperties,
        private val userDetailsService: UserDetailsService,
        private val tokenManager: TokenManager
) : WebSecurityConfigurerAdapter() {
    companion object {
        val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, *jwtProperties.resources.toTypedArray()).permitAll()
                .antMatchers(HttpMethod.GET, *jwtProperties.endpoints.unsecureGet.toTypedArray()).permitAll()
                .antMatchers(HttpMethod.POST, *jwtProperties.endpoints.unsecurePost.toTypedArray()).permitAll()
                .antMatchers(HttpMethod.PUT, *jwtProperties.endpoints.unsecurePut.toTypedArray()).permitAll()
                .antMatchers(HttpMethod.DELETE, *jwtProperties.endpoints.unsecureDelete.toTypedArray()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager(), tokenManager))
                .addFilter(JWTAuthorizationFilter(authenticationManager(), tokenManager, userDetailsService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}