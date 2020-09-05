package com.gnirps.jwt.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import java.util.*


@Configuration
class SwaggerConfigurationExtended {
    @Primary
    @Bean
    fun securitySchemesExtended(): ArrayList<out SecurityScheme> {
        val apiKeys: ArrayList<ApiKey> = ArrayList<ApiKey>()
        apiKeys.add(ApiKey("Bearer", "Authorization", "header"))
        return apiKeys
    }
}