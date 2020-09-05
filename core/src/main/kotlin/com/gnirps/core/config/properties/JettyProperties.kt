package com.gnirps.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.core.jetty")
data class JettyProperties(val idleTimeout: String)