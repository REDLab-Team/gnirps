package com.gnirps.jwt.config

import com.gnirps.jwt.model.Role
import com.gnirps.jwt.service.RoleService
import com.gnirps.logging.service.Logger
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

/**
 * Manages the creation of the two basic roles for using the API, an administrator and a simple user.
 */
@Configuration("RoleMaker")
class RoleMaker(
        private val logger: Logger,
        private val roleService: RoleService
) {
    @PostConstruct
    fun create() {
        if (!roleService.existsByName(SecurityConstants.ADMIN)) {
            val role = Role(
                    name = SecurityConstants.ADMIN,
                    description = "the dude who administrates"
            )
            roleService.createRole(role)
            logger.info("role " + role.toString() + " created")
        }
        if (!roleService.existsByName(SecurityConstants.BASIC_USER)) {
            val role = Role(
                    name = SecurityConstants.BASIC_USER,
                    description = "the dude who doesn't administrate"
            )
            roleService.createRole(role)
            logger.info("role " + role.toString() + " created")
        }
    }
}