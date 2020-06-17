package com.gnirps.core.config

import com.gnirps.core.controller.RestTemplateResponseErrorHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
                .errorHandler(RestTemplateResponseErrorHandler())
                .build()
    }
}
