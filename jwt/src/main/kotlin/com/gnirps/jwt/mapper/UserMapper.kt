package com.gnirps.jwt.mapper

import com.gnirps.jwt.config.SecurityConfiguration
import com.gnirps.jwt.dto.UserRequest
import com.gnirps.jwt.dto.UserResponse
import com.gnirps.jwt.model.User
import java.util.*

/**
 * Links the type of incoming data (UserRequest), the data model (User) and the outgoing data (UserResponse).
 */
class UserMapper {
    companion object {
        fun fromRequest(userRequest: UserRequest): User {
            return User(
                    id = UUID.randomUUID(),
                    password = SecurityConfiguration.passwordEncoder.encode(userRequest.password),
                    firstName = userRequest.firstName,
                    lastName = userRequest.lastName,
                    email = userRequest.email,
                    description = userRequest.description,
                    roles = userRequest.roles
            )
        }

        fun toResponse(user: User): UserResponse {
            return UserResponse(
                    id = user.id,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    description = user.description,
                    roles = user.roles
            )
        }
    }
}