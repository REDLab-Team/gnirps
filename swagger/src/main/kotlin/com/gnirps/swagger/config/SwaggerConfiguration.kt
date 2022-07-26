package com.gnirps.swagger.config

import com.gnirps.swagger.config.properties.SwaggerProperties
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.GroupedOpenApi
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration("SwaggerConfiguration")
@ConfigurationPropertiesScan(basePackages = ["com.gnirps.swagger.config.properties"])
class SwaggerConfiguration(val swaggerProperties: SwaggerProperties) {
    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group(swaggerProperties.basePackage)
            .pathsToMatch(swaggerProperties.api.exposedEndpoints.joinToString("|"))
            .build()
    }
    @Bean
    fun gnirpsOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title(
                    swaggerProperties.api.title
                )
                    .description(swaggerProperties.api.description)
                    .version(swaggerProperties.api.version)
                    .license(License().name(swaggerProperties.api.license).url(swaggerProperties.api.licenseUrl))
            )
    }
}
