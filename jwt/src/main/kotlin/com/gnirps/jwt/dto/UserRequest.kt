package com.gnirps.jwt.dto

import com.gnirps.jwt.model.Role

import javax.persistence.ManyToMany
import javax.validation.constraints.NotBlank

/**
 * Type of data used in inbound queries.
 */
data class UserRequest(
        @NotBlank val password: String,
        @NotBlank val email: String,
        @ManyToMany
        @NotBlank val roles: Set<Role>,
        val firstName: String?,
        val lastName: String?,
        val description: String?
)