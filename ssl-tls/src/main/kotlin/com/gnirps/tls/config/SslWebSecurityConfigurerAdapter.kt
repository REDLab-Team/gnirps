package com.gnirps.tls.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@EnableWebSecurity
class SslWebSecurityConfigurerAdapter : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http
                .requiresChannel().anyRequest().requiresSecure()
                .and().authorizeRequests().antMatchers("/**").permitAll()
    }
}
