package com.gnirps.jwt.service

import com.gnirps.jwt.model.User
import com.gnirps.jwt.repository.UserRepository
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

/**
 * Provides the various functions associated with the User.
 */
@Service
@Transactional
@Primary
class UserService(
        val userRepository: UserRepository
) : UserDetailsService {

    fun findAll(): MutableList<User> {
        return userRepository.findAll()
    }

    @Throws(EntityExistsException::class)
    fun createUser(user: User): User {
        return userRepository.saveAndFlush(user)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteUser(userId: UUID) {
        if (!userRepository.existsById(userId)) {
            throw EntityNotFoundException("User $userId not found")
        }
        userRepository.deleteById(userId)
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException(email)
        return org.springframework.security.core.userdetails.User(
                user.email,
                user.password,
                user.authorities
        )
    }
}