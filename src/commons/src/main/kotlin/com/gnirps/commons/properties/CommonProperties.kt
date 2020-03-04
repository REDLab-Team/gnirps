package com.gnirps.commons.properties

import com.gnirps.commons.config.properties.YamlPropertySourceFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@Configuration
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
class CommonProperties