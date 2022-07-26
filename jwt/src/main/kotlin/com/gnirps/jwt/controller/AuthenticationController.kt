package com.gnirps.jwt.controller

import com.gnirps.jwt.config.SecurityConstants
import com.gnirps.jwt.dto.LoginRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Manages the endpoints related to the connection and disconnection of a user.
 */
@RestController
@RequestMapping(value = ["/"], produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthenticationController {
    @Operation(summary = SecurityConstants.LOGIN_ENDPOINT)
    @ApiResponse(responseCode = "200", description = "")
    @PostMapping(value = [SecurityConstants.LOGIN_ENDPOINT])
    fun login(@RequestBody loginRequestDto: LoginRequest) {
        throw IllegalStateException("missing Spring Security's authentication handling")
    }

    @Operation(summary = SecurityConstants.LOGOUT_ENDPOINT, security = [SecurityRequirement(name = "Bearer")])
    @ApiResponse(responseCode = "200", description = "")
    @PostMapping(value = [SecurityConstants.LOGOUT_ENDPOINT])
    fun logout() {
        throw IllegalStateException("missing Spring Security's authentication handling")
    }
}
