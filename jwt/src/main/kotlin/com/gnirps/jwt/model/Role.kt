package com.gnirps.jwt.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class Role(
        @Id @Column(length = 10)
        @NotBlank val name: String,
        val description: String?
)