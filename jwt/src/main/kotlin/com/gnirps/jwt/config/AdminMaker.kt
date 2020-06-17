package com.gnirps.jwt.config

import com.gnirps.jwt.config.properties.JWTProperties
import com.gnirps.logging.service.Logger
import com.gnirps.jwt.model.Role
import com.gnirps.jwt.model.User
import com.gnirps.jwt.service.RoleService
import com.gnirps.jwt.service.UserService
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashSet

/**
 * Manages the creation of the administrator at database launch;
 * this is the only user available at launch.
 */
@Configuration
@ConfigurationPropertiesScan(basePackages = ["com.gnirps.jwt.config.properties"])
@DependsOn("RoleMaker")
class AdminMaker (
        private val logger : Logger,
        private val jwtProperties: JWTProperties,
        private val roleService: RoleService,
        private val userService: UserService
){
    @PostConstruct
    fun create() {
        if (!userService.existsByEmail(jwtProperties.admin.email)) {
            val newRoles = HashSet<Role?>()
            newRoles.add(roleService.findByName(SecurityConstants.ADMIN))
            val admin = User(
                id = UUID.randomUUID(),
                email = jwtProperties.admin.email,
                password = WebSecurity.passwordEncoder.encode(jwtProperties.admin.password),
                roles = newRoles,
                firstName = "",
                lastName = "",
                description = ""
            )
            userService.createUser(admin)
            logger.info("user " + admin.toString() + " created")
        }
    }
}