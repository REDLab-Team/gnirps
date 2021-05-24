package com.gnirps.cors.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.cors")
data class CorsProperties(
        val accessControlAllowOrigin: String,
        val accessControlAllowCredentials: String,
        val accessControlAllowMethods: String,
        val accessControlAllowHeaders: String,
        val accessControlMaxAge: String
)