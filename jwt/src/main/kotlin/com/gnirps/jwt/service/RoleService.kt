package com.gnirps.jwt.service

import com.gnirps.jwt.model.Role
import com.gnirps.jwt.repository.RoleRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

/**
 * Provides the various functions associated with the Role.
 */
@Service
@Transactional
class RoleService(
        val roleRepository: RoleRepository
) {
    fun findAll(): List<Role> {
        return roleRepository.findAll()
    }

    fun findByName(name: String): Role {
        return roleRepository.findByName(name) ?: throw EntityNotFoundException("role $name not found")
    }

    @Throws(EntityExistsException::class)
    fun createRole(role: Role): Role {
        return roleRepository.saveAndFlush(role)
    }

    @Throws(EntityNotFoundException::class)
    fun deleteRole(role: String) {
        if (!roleRepository.existsByName(role)) {
            throw EntityNotFoundException("Role $role not found")
        }
        roleRepository.deleteByName(role)
    }

    fun existsByName(name: String): Boolean {
        return roleRepository.existsByName(name)
    }
}