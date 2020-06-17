package com.gnirps.jwt.controller

import com.gnirps.jwt.config.SecurityConstants
import com.gnirps.jwt.dto.LoginRequest
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
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
    @ApiOperation(value = SecurityConstants.LOGIN_ENDPOINT)
    @ApiResponses(ApiResponse(code = 200, message = "", response = Authentication::class))
    @PostMapping(value = [SecurityConstants.LOGIN_ENDPOINT])
    fun login(@RequestBody loginRequestDto: LoginRequest) {
        throw IllegalStateException("missing Spring Security's authentication handling")
    }

    @ApiOperation(value = SecurityConstants.LOGOUT_ENDPOINT)
    @ApiResponses(ApiResponse(code = 200, message = ""))
    @PostMapping(value = [SecurityConstants.LOGOUT_ENDPOINT])
    fun logout() {
        throw IllegalStateException("missing Spring Security's authentication handling")
    }
}