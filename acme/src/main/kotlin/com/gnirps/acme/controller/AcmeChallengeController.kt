package com.gnirps.acme.controller

import com.gnirps.acme.service.AcmeService
import com.gnirps.logging.service.Logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.shredzone.acme4j.exception.AcmeException
import org.springframework.context.annotation.DependsOn
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Acme Controller",
    description = "Handle Acme Challenges."
)
@RestController
@DependsOn("methodSecurityConfigurationBean")
@Order(Ordered.LOWEST_PRECEDENCE)
@RequestMapping(AcmeChallengeController.ROOT_PATH)
class AcmeChallengeController(
    private val acmeService: AcmeService,
    private val logger: Logger
) {
    companion object {
        const val ROOT_PATH: String = "/.well-known/acme-challenge"
    }

    @GetMapping("/{token}")
    @Operation(summary = "Retrieve a challenge's content.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Challenge retrieved"),
            ApiResponse(responseCode = "404", description = "Challenge not found")
        ]

    )
    @ResponseStatus(HttpStatus.OK)
    @Throws(AcmeException::class)
    fun find(@PathVariable token: String): String {
        logger.info("Acme challenge received for token $token.")
        return acmeService.getAuthorization(token)
    }

    @GetMapping("/trigger")
    @Operation(summary = "Trigger certificate's generation")
    @ApiResponse(responseCode = "200", description = "Certificate created")
    @ResponseStatus(HttpStatus.OK)
    @Throws(AcmeException::class)
    fun generateCertificate() {
        acmeService.generateCertificate()
    }
}
