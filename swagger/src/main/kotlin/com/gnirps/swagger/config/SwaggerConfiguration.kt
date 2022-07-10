package com.gnirps.swagger.config

import com.gnirps.swagger.config.properties.SwaggerProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket


@Configuration("SwaggerConfiguration")
@ConfigurationPropertiesScan(basePackages = ["com.gnirps.swagger.config.properties"])
class SwaggerConfiguration(val swaggerProperties: SwaggerProperties) {
    @Bean
    fun securitySchemes(): ArrayList<out SecurityScheme> {
        return ArrayList()
    }

    @Bean
    fun securityContext(): List<SecurityContext> {
        return emptyList()
    }

    @Bean
    fun docket(
            securitySchemes: ArrayList<out SecurityScheme>,
            securityContexts: List<SecurityContext>
    ): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.basePackage))
                .paths(PathSelectors.regex(swaggerProperties.api.exposedEndpoints.joinToString("|")))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes)
                .securityContexts(securityContexts)
    }

    fun apiInfo(): ApiInfo {
        return ApiInfo(
                swaggerProperties.api.title,
                swaggerProperties.api.description,
                swaggerProperties.api.version,
                swaggerProperties.api.termsOfService,
                Contact(
                        swaggerProperties.maintainer.name,
                        swaggerProperties.maintainer.website,
                        swaggerProperties.maintainer.email
                ),
                swaggerProperties.api.license,
                swaggerProperties.api.licenseUrl,
                emptyList()
        )
    }
}
