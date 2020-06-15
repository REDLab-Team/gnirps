package com.gnirps.acme.config

import com.gnirps.acme.service.AcmeService
import com.gnirps.logging.service.Logger
import org.shredzone.acme4j.exception.AcmeException
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["com.gnirps.acme.config.properties"])
class ApplicationStart(
    private val acmeService: AcmeService,
    private val logger: Logger

):
        ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(
        applicationReadyEvent: ApplicationReadyEvent
    ) {
        try {
            acmeService.generateCertificate()
        } catch (exception: AcmeException) {
            logger.error(exception)
        }
    }
}
