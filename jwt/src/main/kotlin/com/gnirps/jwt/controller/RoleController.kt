package com.gnirps.jwt.controller

import com.gnirps.jwt.annotations.AdminAccess
import com.gnirps.jwt.model.Role
import com.gnirps.jwt.service.RoleService
import com.gnirps.logging.service.Logger
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.Authorization
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
    @ApiOperation(value = "Create a new role.", authorizations = [Authorization(value = "Bearer")])
    fun create(@RequestBody role: Role): Role {
        val newRole: Role = roleService.createRole(role)
        logger.info("role ${newRole} created")
        return newRole
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = ["/{id}"])
    @AdminAccess
    @ApiOperation(value = "Delete matching role.", authorizations = [Authorization(value = "Bearer")])
    fun delete(@PathVariable id: String) {
        roleService.deleteRole(id)
        logger.info("role ${id} deleted")
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @AdminAccess
    @ApiOperation(value = "Get all roles.", authorizations = [Authorization(value = "Bearer")])
    fun findAll(): List<Role>? {
        return roleService.findAll()
    }
}