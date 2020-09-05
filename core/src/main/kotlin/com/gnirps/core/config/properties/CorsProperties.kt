package com.gnirps.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.core.cors")
data class CorsProperties(
        val accessControlAllowOrigin: String,
        val accessControlAllowMethods: String,
        val accessControlAllowHeaders: String,
        val accessControlMaxAge: String
)