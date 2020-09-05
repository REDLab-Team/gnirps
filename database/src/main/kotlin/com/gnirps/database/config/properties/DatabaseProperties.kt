package com.gnirps.database.config.properties

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.database")
data class DatabaseProperties(
        @URL val url: String,
        @NotBlank val username: String,
        @NotBlank val password: String,
        @NotBlank val driverClassName: String,
        @NotBlank val dialect: String,
        @Pattern(regexp = "^(create|create/drop|validate|update)$")
        val hbm2ddlAuto: String,
        @NotBlank val useSqlComments: String,
        @NotBlank val defaultSchema: String,
        @Pattern(
                regexp = "^(org.hibernate.boot.model.naming" +
                        ".PhysicalNamingStrategyStandardImpl|" +
                        "com.gnirps.database.com.gnirps.keycloak.config" +
                        ".CustomPhysicalNamingStrategy" +
                        ")$"
        ) val namingStrategy: String,
        val hibernateAdditionalProperties: Map<String, String>
)