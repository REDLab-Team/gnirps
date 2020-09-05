package com.gnirps.tls.config

import com.gnirps.tls.config.properties.TLSProperties
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.io.File
import javax.net.ssl.SSLContext


@Configuration
@ConfigurationPropertiesScan(basePackageClasses = [TLSProperties::class])
class RestTemplateConfigurationExtended(private val tlsProperties: TLSProperties) {
    @Bean
    fun sslRestTemplate(): RestTemplate {
        return if (tlsProperties.trustStore.path != null) {
            val trustStoreFile = File("trust-store")
            trustStoreFile.outputStream().use { ClassPathResource(tlsProperties.trustStore.path).inputStream.copyTo(it) }
            val sslContext: SSLContext =
                    tlsProperties.trustStore.password?.let {
                        SSLContextBuilder()
                                .loadTrustMaterial(trustStoreFile, it.toCharArray())
                                .build()
                    } ?: SSLContextBuilder()
                                .loadTrustMaterial(trustStoreFile)
                                .build()
            val factory = HttpComponentsClientHttpRequestFactory(
                    HttpClients
                            .custom()
                            .setSSLSocketFactory(SSLConnectionSocketFactory(sslContext))
                            .build()
            )
            RestTemplate(factory)
        } else {
            RestTemplateBuilder().build()
        }
    }
}
