package com.gnirps.jwt.config

import com.gnirps.swagger.config.SwaggerConfiguration
import com.gnirps.swagger.config.properties.SwaggerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
class SwaggerConfigurationExtended(override val swaggerProperties: SwaggerProperties) :
        SwaggerConfiguration(swaggerProperties) {
    @Bean
    override fun docket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.basePackage))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
    }

    private fun securitySchemes(): ArrayList<out SecurityScheme?>? {
        val apiKeys: ArrayList<ApiKey> = ArrayList<ApiKey>()
        apiKeys.add(ApiKey("Bearer", "Authorization", "header"))
        return apiKeys
    }
}