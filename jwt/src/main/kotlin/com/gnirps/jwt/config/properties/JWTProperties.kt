package com.gnirps.jwt.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.validation.constraints.Email

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.jwt")
data class JWTProperties(
        val privateKey: String,
        val url: String,
        val endpoints: Endpoints,
        val admin: Admin,
        val resources: List<String>
) {
    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.jwt.endpoints")
    data class Endpoints(
            val unsecurePost: List<String>,
            val unsecureGet: List<String>,
            val unsecurePut: List<String>,
            val unsecureDelete: List<String>
    )

    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.jwt.admin")
    data class Admin(
            @Email val email: String,
            val password: String
    )
}