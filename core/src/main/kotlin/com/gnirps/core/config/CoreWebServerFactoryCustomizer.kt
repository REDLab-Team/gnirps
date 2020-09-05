package com.gnirps.core.config

import com.gnirps.core.config.properties.JettyProperties
import com.gnirps.logging.service.Logger
import org.eclipse.jetty.server.AbstractConnector
import org.eclipse.jetty.webapp.AbstractConfiguration
import org.eclipse.jetty.webapp.WebAppContext
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
class CoreWebServerFactoryCustomizer(
        private val logger: Logger,
        private val jettyProperties: JettyProperties,
        private val customJettyErrorHandler: CustomJettyErrorHandler
) : WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    override fun customize(factory: JettyServletWebServerFactory) {
        factory.addConfigurations(
                object : AbstractConfiguration() {
                    override fun configure(context: WebAppContext) {
                        context.errorHandler = customJettyErrorHandler
                    }
                }
        )

        factory.serverCustomizers.add(
                JettyServerCustomizer { server ->
                    for (connector in server.connectors) {
                        if (connector is AbstractConnector) {
                            connector.idleTimeout = jettyProperties.idleTimeout.toLong()
                            logger.debug("jetty timeout set to ${jettyProperties.idleTimeout}")
                        }
                    }
                }
        )
    }
}