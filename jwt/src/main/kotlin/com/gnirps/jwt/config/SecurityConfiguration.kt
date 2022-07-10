package com.gnirps.jwt.config

import com.gnirps.jwt.config.properties.JWTProperties
import com.gnirps.jwt.util.TokenManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


/**
 * Implements a security layer on incoming http requests by determining the protected routes and filters to be applied.
 * TODO https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
@Configuration
class SecurityConfiguration(
        private val jwtProperties: JWTProperties,
        private val userDetailsService: UserDetailsService,
        private val tokenManager: TokenManager,
        private val authenticationManager: AuthenticationManager
) {
    companion object {
        val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = passwordEncoder

    @Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(builder: AuthenticationManagerBuilder, encoder: PasswordEncoder): AuthenticationManager {
        return builder.userDetailsService(userDetailsService).passwordEncoder(encoder).and().build()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors().and().csrf().disable()
            .authorizeHttpRequests {
                    auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                auth
                    .antMatchers(HttpMethod.POST, SecurityConstants.LOGIN_ENDPOINT).permitAll()
                    .antMatchers(HttpMethod.GET, *jwtProperties.resources.toTypedArray()).permitAll()
                    .antMatchers(HttpMethod.GET, *jwtProperties.endpoints.unsecureGet.toTypedArray()).permitAll()
                    .antMatchers(HttpMethod.POST, *jwtProperties.endpoints.unsecurePost.toTypedArray()).permitAll()
                    .antMatchers(HttpMethod.PUT, *jwtProperties.endpoints.unsecurePut.toTypedArray()).permitAll()
                    .antMatchers(HttpMethod.DELETE, *jwtProperties.endpoints.unsecureDelete.toTypedArray()).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(JWTAuthenticationFilter(authenticationManager, tokenManager))
                    .addFilter(JWTAuthorizationFilter(authenticationManager, tokenManager, userDetailsService))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .httpBasic(withDefaults())
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}