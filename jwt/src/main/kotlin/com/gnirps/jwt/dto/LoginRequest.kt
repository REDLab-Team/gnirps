package com.gnirps.jwt.dto

data class LoginRequest(
        val username: String = "",
        val password: String = ""
)