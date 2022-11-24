package com.gnirps.jwt.controller

import com.gnirps.jwt.annotations.AdminAccess
import com.gnirps.jwt.config.SecurityConstants
import com.gnirps.jwt.dto.UserRequest
import com.gnirps.jwt.dto.UserResponse
import com.gnirps.jwt.mapper.UserMapper
import com.gnirps.jwt.model.User
import com.gnirps.jwt.service.UserService
import com.gnirps.logging.service.Logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Manages the endpoints related to the users.
 */
@RestController
@RequestMapping("/users")
class UserController(
        private val logger: Logger,
        private val userService: UserService
) {
    @AdminAccess
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @PreAuthorize("hasRole('" + SecurityConstants.ADMIN + "')")
    @Operation(summary = "Get all users.", security = [SecurityRequirement(name = "Bearer")])
    fun findAll(): List<UserResponse>? {
        return userService.findAll().map { UserMapper.toResponse(it) }
    }

    @AdminAccess
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('" + SecurityConstants.ADMIN + "')")
    @Operation(summary = "Create a new user.", security = [SecurityRequirement(name = "Bearer")])
    fun create(@RequestBody userRequest: UserRequest): UserResponse {
        val user: User = userService.createUser(UserMapper.fromRequest(userRequest))
        logger.info("$user created")
        return UserMapper.toResponse(user)
    }

    @AdminAccess
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = ["/{id}"])
    @PreAuthorize("hasRole('" + SecurityConstants.ADMIN + "')")
    @Operation(summary = "Delete matching user.", security = [SecurityRequirement(name = "Bearer")])
    fun delete(@PathVariable id: String) {
        userService.deleteUser(UUID.fromString(id))
        logger.info("user $id deleted")
    }
}
