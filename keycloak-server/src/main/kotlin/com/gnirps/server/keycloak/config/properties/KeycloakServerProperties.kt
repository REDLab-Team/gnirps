package com.gnirps.server.keycloak.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.keycloak.server")
data class KeycloakServerProperties(
        val masterRealm: MasterRealm,
        val realmsFromJson: List<String>,
        val contextPath: String
) {
    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.keycloak.server.master-realm")
    data class MasterRealm(
            val adminUsername: String,
            val adminPassword: String
    )
}