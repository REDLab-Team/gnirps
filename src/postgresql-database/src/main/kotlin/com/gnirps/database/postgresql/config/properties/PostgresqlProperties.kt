package com.gnirps.database.postgresql.config.properties

import com.gnirps.commons.config.properties.YamlPropertySourceFactory
import org.hibernate.validator.constraints.URL
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Configuration
@Validated
@PropertySources(
    PropertySource(
        "application-postgresql.yml",
        factory = YamlPropertySourceFactory::class
    ),
    PropertySource(
        "application.yml",
        ignoreResourceNotFound = true,
        factory = YamlPropertySourceFactory::class
    )
)
class PostgresqlProperties {
    @URL
    @Value("\${gnirps.database.postgresql.url}")
    lateinit var url: String

    @NotBlank
    @Value("\${gnirps.database.postgresql.username}")
    lateinit var username: String

    @NotBlank
    @Value("\${gnirps.database.postgresql.password}")
    lateinit var password: String

    @NotBlank
    @Value("\${gnirps.database.postgresql.driver-class-name}")
    lateinit var driverClassName: String

    @NotBlank
    @Value("\${gnirps.database.postgresql.database-dialect}")
    lateinit var databaseDialect: String

    @Value("\${gnirps.database.postgresql.hbm2ddl-auto}")
    @Pattern(regexp = "^(create|create/drop|validate|update)$")
    lateinit var hbm2ddlAuto: String

    @NotBlank
    @Value("\${gnirps.database.postgresql.use-sql-comments}")
    lateinit var useSqlCommments: String

    @NotBlank
    @Value("\${gnirps.database.postgresql.default-schema}")
    lateinit var defaultSchema: String

    @Value("\${gnirps.database.postgresql.naming-strategy}")
    @Pattern(
        regexp = "^(org.hibernate.boot.model.naming" +
                ".PhysicalNamingStrategyStandardImpl|" +
                "com.gnirps.database.postgresql.config" +
                ".CustomPhysicalNamingStrategy" +
                ")$"
    )
    lateinit var namingStrategy: String
}
