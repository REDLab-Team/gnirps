package com.gnirps.swagger.config.properties

import io.swagger.v3.oas.models.security.SecurityScheme
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
    val rootRedirect: String,
    val securitySchemes: Map<String, SecuritySchemeProperties> = emptyMap()
) {
    data class Api(
        val title: String,
        val description: String,
        val version: String,
        val license: String,
        val licenseUrl: String,
        val termsOfService: String,
        val exposedEndpoints: Array<String>,
    )

    data class Maintainer(
        val name: String,
        @Email val email: String,
        val website: String
    )

    data class SecuritySchemeProperties(
        val type: SecurityScheme.Type,
        val scheme: String? = null,
        val bearerFormat: String? = null,
        val `in`: SecurityScheme.In? = null,
        val name: String? = null
    )
}
