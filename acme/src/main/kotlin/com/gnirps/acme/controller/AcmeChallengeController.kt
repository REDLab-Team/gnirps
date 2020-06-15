package com.gnirps.acme.controller

import com.gnirps.acme.service.AcmeService
import com.gnirps.logging.service.Logger
import io.swagger.annotations.*
import org.shredzone.acme4j.exception.AcmeException
import org.springframework.context.annotation.DependsOn
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Api(
    tags = ["Acme Controller"],
    value = "Acme Controller",
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
    @ApiOperation("Retrieve a challenge's content.")
    @ApiResponses(
        ApiResponse(code = 200, message = "Challenge retrieved"),
        ApiResponse(code = 404, message = "Challenge not found")
    )
    @ResponseStatus(HttpStatus.OK)
    @Throws(AcmeException::class)
    fun find(@PathVariable token: String): String {
        logger.info("Acme challenge received for token $token.")
        return acmeService.getAuthorization(token)
    }

    @GetMapping("/trigger")
    @ApiOperation("Trigger certificate's generation")
    @ApiResponses(
        ApiResponse(code = 200, message = "Certificate created")
    )
    @ResponseStatus(HttpStatus.OK)
    @Throws(AcmeException::class)
    fun generateCertificate() {
        acmeService.generateCertificate()
    }
}
