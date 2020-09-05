package com.gnirps.tls.config

import com.gnirps.logging.service.Logger
import com.gnirps.tls.config.properties.TLSProperties
import org.eclipse.jetty.http.HttpVersion
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import java.io.File

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = [TLSProperties::class])
class SslWebServerFactoryCustomizer(
        private val logger: Logger,
        private val tlsProperties: TLSProperties,
        private val resourceLoader: ResourceLoader
) : WebServerFactoryCustomizer<JettyServletWebServerFactory> {
    override fun customize(factory: JettyServletWebServerFactory) {
        factory.addServerCustomizers(
                JettyServerCustomizer { server ->
                    val sslContextFactory = SslContextFactory.Server()
                    sslContextFactory.keyStoreResource = loadClassPathResource(tlsProperties.keyStore.path)
                    sslContextFactory.keyStoreType = tlsProperties.keyStore.type
                    sslContextFactory.setKeyStorePassword(tlsProperties.keyStore.password)
                    tlsProperties.trustStore.path?.let { path ->
                        sslContextFactory.trustStoreResource = loadClassPathResource(path)
                        tlsProperties.trustStore.type?.let { sslContextFactory.trustStoreType = it }
                        tlsProperties.trustStore.password?.let { sslContextFactory.setTrustStorePassword(it) }
                    }

                    val httpsConfiguration = HttpConfiguration()
                    httpsConfiguration.addCustomizer(SecureRequestCustomizer())

                    val httpsConnector = ServerConnector(
                            server,
                            SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                            HttpConnectionFactory(httpsConfiguration)
                    )
                    httpsConnector.port = tlsProperties.port

                    server.addConnector(httpsConnector)
                }
        )

        val loggedProperties = HashMap<String, Any>()
        loggedProperties["https-port"] = tlsProperties.port
        loggedProperties["key-store"] = tlsProperties.keyStore.path
        loggedProperties["trust-store"] = tlsProperties.trustStore.path ?: "none"
        logger.debug("Jetty servlet configuration: $tlsProperties")
    }

    private fun loadClassPathResource(path: String): Resource =
            Resource.newResource(
                    File("$path.jetty").let { file ->
                        file.outputStream().use { resourceLoader.getResource(path).inputStream.copyTo(it) }
                        file
                    }
            )
}