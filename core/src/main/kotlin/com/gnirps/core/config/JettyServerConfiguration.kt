package com.gnirps.core.config

import com.gnirps.core.config.properties.JettyProperties
import com.gnirps.logging.service.Logger
import org.eclipse.jetty.server.AbstractConnector
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JettyServerConfiguration(
        private val jettyProperties: JettyProperties,
        private val logger: Logger
) {
    @Bean
    fun jettyCustomizer(): JettyServerCustomizer {
        return JettyServerCustomizer { server ->
            for (connector in server.connectors) {
                if (connector is AbstractConnector) {
                    connector.idleTimeout = jettyProperties.idleTimeout.toLong()
                    logger.info("jetty timeout set to ${jettyProperties.idleTimeout} for connector ${connector.name}")
                }
            }
        }
    }
}