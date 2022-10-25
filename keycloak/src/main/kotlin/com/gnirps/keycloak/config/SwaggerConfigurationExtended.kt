package com.gnirps.keycloak.config

import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
class SwaggerConfigurationExtended {
    @Primary
    @Bean
    fun securitySchemesExtended(): Map<String, SecurityScheme> {
        val securityScheme: SecurityScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        return mapOf("Authorization" to securityScheme)
    }
}