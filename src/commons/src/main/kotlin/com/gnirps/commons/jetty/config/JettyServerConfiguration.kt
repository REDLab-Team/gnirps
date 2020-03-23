package com.gnirps.commons.jetty.config

import com.gnirps.commons.jetty.config.properties.JettyProperties
import org.eclipse.jetty.server.AbstractConnector
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JettyServerConfiguration(val jettyProperties: JettyProperties) {
    @Bean
    fun jettyCustomizer(): JettyServerCustomizer {
        return JettyServerCustomizer { server ->
            for (connector in server.connectors) {
                if (connector is AbstractConnector) {
                    connector.idleTimeout = jettyProperties.idleTimeout.toLong()
                }
            }
        }
    }
}