package com.gnirps.swagger.config

import com.gnirps.swagger.config.properties.SwaggerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration(val swaggerProperties: SwaggerProperties) {
    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.basePackage))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            swaggerProperties.apiTitle,
            swaggerProperties.apiDescription,
            swaggerProperties.apiVersion,
            swaggerProperties.apiTermsOfService,
            Contact(
                swaggerProperties.maintainerName,
                swaggerProperties.maintainerWebsite,
                swaggerProperties.maintainerEmail
            ),
            swaggerProperties.apiLicense,
            swaggerProperties.apiLicenseUrl,
            emptyList()
        )
    }
}
