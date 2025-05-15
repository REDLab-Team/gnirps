package com.gnirps.swagger.config

import com.gnirps.swagger.config.properties.SwaggerProperties
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
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
        val openAPI = OpenAPI()
            .info(
                Info()
                    .title(swaggerProperties.api.title)
                    .contact(
                        Contact()
                            .name(swaggerProperties.maintainer.name)
                            .email(swaggerProperties.maintainer.email)
                            .url(swaggerProperties.maintainer.website)
                    )
                    .description(swaggerProperties.api.description)
                    .version(swaggerProperties.api.version)
                    .license(License().name(swaggerProperties.api.license).url(swaggerProperties.api.licenseUrl))
            )

        if (swaggerProperties.securitySchemes.isNotEmpty()) {
            val components = Components()

            swaggerProperties.securitySchemes.forEach { (name, config) ->
                val scheme = SecurityScheme().type(config.type)
                    .also {
                        config.scheme?.let { s -> it.scheme = s }
                        config.bearerFormat?.let { bf -> it.bearerFormat = bf }
                        config.`in`?.let { i -> it.`in` = i }
                        config.name?.let { n -> it.name = n }
                    }

                components.addSecuritySchemes(name, scheme)
            }

            openAPI.components(components)
        }

        return openAPI
    }
}
