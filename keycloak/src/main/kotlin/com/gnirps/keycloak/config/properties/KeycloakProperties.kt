package com.gnirps.keycloak.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.keycloak")
data class KeycloakProperties(
        val resources: List<String>,
        val endpoints: Endpoints,
        val appName: String
) {
    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.keycloak.endpoints")
    data class Endpoints(
            val unsecurePost: List<String>,
            val unsecureGet: List<String>,
            val unsecurePut: List<String>,
            val unsecureDelete: List<String>
    )
}