package com.gnirps.core.config

import com.gnirps.core.controller.RestTemplateResponseErrorHandler
import com.gnirps.logging.service.Logger
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestTemplate

@Configuration
class CoreRestTemplateCustomizer(
        private val logger: Logger,
        private val restTemplateErrorHandler: RestTemplateResponseErrorHandler
) : RestTemplateCustomizer {
    override fun customize(restTemplate: RestTemplate) {
        restTemplate.interceptors.add(CustomClientHttpRequestInterceptor(logger))
        restTemplate.errorHandler = restTemplateErrorHandler
    }

    class CustomClientHttpRequestInterceptor(private val logger: Logger) : ClientHttpRequestInterceptor {
        override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
            logger.debug(
                    "{" +
                            "headers: ${request.headers}, " +
                            "method: ${request.method}, " +
                            "uri: ${request.uri}" +
                            "}"
            )
            return execution.execute(request, body)
        }
    }

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder().build()
}
