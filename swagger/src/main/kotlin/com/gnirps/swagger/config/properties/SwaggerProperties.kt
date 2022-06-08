package com.gnirps.swagger.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Email

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.swagger")
data class SwaggerProperties(
        val basePackage: String,
        val api: Api,
        val maintainer: Maintainer,
        val rootRedirect: String
) {
    data class Api(
            val title: String,
            val description: String,
            val version: String,
            val license: String,
            val licenseUrl: String,
            val termsOfService: String,
            val allowedPaths: Array<String>,
            )

    data class Maintainer(
            val name: String,
            @Email val email: String,
            val website: String
    )
}
