package com.gnirps.acme.config

import com.gnirps.acme.service.AcmeService
import com.gnirps.commons.logging.service.Logger
import org.shredzone.acme4j.exception.AcmeException
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
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
