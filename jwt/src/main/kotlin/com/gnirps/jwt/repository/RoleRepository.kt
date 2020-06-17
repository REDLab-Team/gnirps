package com.gnirps.jwt.repository

import com.gnirps.jwt.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Defines and creates the database associated with the Roles using JPA and Hibernate.
 * Also manages the various functions associated with the Roles.
 */
@Repository
interface RoleRepository : JpaRepository<Role, String> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): Role?
    fun deleteByName(name: String)
}