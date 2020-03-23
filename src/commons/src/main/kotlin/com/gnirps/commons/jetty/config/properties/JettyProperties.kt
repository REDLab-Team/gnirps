package com.gnirps.commons.jetty.config.properties

import com.gnirps.commons.config.properties.YamlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.validation.annotation.Validated

@Configuration
@Validated
@PropertySources(
        PropertySource(
                "application-commons.yml",
                factory = YamlPropertySourceFactory::class
        ),
        PropertySource(
                "application.yml",
                ignoreResourceNotFound = true,
                factory = YamlPropertySourceFactory::class
        )
)
class JettyProperties {
    @Value("\${gnirps.commons.jetty.idle-timeout}")
    lateinit var idleTimeout: String
}