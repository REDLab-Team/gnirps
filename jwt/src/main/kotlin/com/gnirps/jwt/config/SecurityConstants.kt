package com.gnirps.jwt.config

class SecurityConstants {
    companion object {
        const val EXPIRATION_TIME_IN_SECOND: Long = 864000
        const val TOKEN_PREFIX = "Bearer "
        const val HEADER_STRING = "Authorization"
        const val AUTHORITIES_KEY = "scopes"

        const val ADMIN = "ADMIN"
        const val BASIC_USER = "BASIC_USER"

        const val LOGIN_ENDPOINT = "/login"
        const val LOGOUT_ENDPOINT = "/logout"
    }
}