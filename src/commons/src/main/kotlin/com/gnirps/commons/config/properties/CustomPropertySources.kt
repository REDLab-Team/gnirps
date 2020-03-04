package com.gnirps.commons.config.properties

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Configuration
@PropertySources(
        PropertySource(
                "application-commons.yml",
                factory = YamlPropertySourceFactory::class
        ),
        PropertySource(
                "application-swagger.yml",
                factory = YamlPropertySourceFactory::class
        ),
        PropertySource(
                "application-logging.yml",
                ignoreResourceNotFound = true,
                factory = YamlPropertySourceFactory::class
        ),
        PropertySource(
                "application-oauth2.yml",
                ignoreResourceNotFound = true,
                factory = YamlPropertySourceFactory::class
        ),
        PropertySource(
                "application.yml",
                ignoreResourceNotFound = true,
                factory = YamlPropertySourceFactory::class
        )
)
annotation class CustomPropertySources
