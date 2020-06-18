package com.gnirps.jwt.model

import org.hibernate.annotations.Type
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
data class User(
        @Id @Type(type = "uuid-char")
        @Column(columnDefinition = "VARCHAR(37)")
        @NotNull val id: UUID,
        @NotBlank private val password: String,

        @NotBlank val email: String,

        @ManyToMany
        @NotBlank val roles: Set<Role?>,

        val firstName: String?,
        val lastName: String?,
        val description: String?
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority?>? {
        return roles
                .stream()
                .map { role -> SimpleGrantedAuthority("ROLE_" + role?.name) }
                .collect(Collectors.toSet())
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return email
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun getPassword(): String {
        return password
    }
}






