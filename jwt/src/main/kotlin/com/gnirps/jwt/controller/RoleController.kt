package com.gnirps.jwt.controller

import com.gnirps.jwt.annotations.AdminAccess
import com.gnirps.jwt.model.Role
import com.gnirps.jwt.service.RoleService
import com.gnirps.logging.service.Logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 *  * Manages the endpoints related to the users roles.
 */
@RestController
@RequestMapping("/roles")
class RoleController(
    private val logger: Logger,
    private val roleService: RoleService
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @AdminAccess
    @Operation(summary = "Create a new role.", security = [SecurityRequirement(name = "Bearer")])
    fun create(@RequestBody role: Role): Role {
        val newRole: Role = roleService.createRole(role)
        logger.info("role ${newRole} created")
        return newRole
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = ["/{id}"])
    @AdminAccess
    @Operation(summary = "Delete matching role.", security = [SecurityRequirement(name = "Bearer")])
    fun delete(@PathVariable id: String) {
        roleService.deleteRole(id)
        logger.info("role ${id} deleted")
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @AdminAccess
    @Operation(summary = "Get all roles.", security = [SecurityRequirement(name = "Bearer")])
    fun findAll(): List<Role>? {
        return roleService.findAll()
    }
}
