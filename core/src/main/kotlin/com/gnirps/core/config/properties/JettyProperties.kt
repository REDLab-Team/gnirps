package com.gnirps.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.PropertySource

@PropertySource("classpath:application-core.yml")
@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.core.jetty")
data class JettyProperties(val idleTimeout: String)