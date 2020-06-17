package com.gnirps.jwt.dto

import com.gnirps.jwt.model.Role
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * Type of data used in outbound queries.
 */
data class UserResponse(
        @Id @Type(type = "uuid-char")
        @Column(columnDefinition = "VARCHAR(37)")
        @NotNull val id: UUID,
        @NotBlank val email: String,
        @ManyToMany
        @NotBlank val roles: Set<Role>,
        val firstName: String?,
        val lastName: String?,
        val description: String?
)