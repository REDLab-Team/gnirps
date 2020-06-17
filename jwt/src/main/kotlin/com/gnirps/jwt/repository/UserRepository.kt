package com.gnirps.jwt.repository

import com.gnirps.jwt.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Defines and creates the database associated with Users using JPA and Hibernate.
 * Also manages the different functions associated to the Users.
 */
@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}