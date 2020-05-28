package com.gnirps.database.properties

import com.gnirps.commons.config.properties.CustomPropertySources
import org.hibernate.validator.constraints.URL
import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Validated
@CustomPropertySources
class DatabaseProperties {
    @URL
    @Value("\${gnirps.database.url}")
    lateinit var dataBaseUrl: String

    @NotBlank
    @Value("\${gnirps.database.user}")
    lateinit var dataBaseUser: String

    @NotBlank
    @Value("\${gnirps.database.password}")
    lateinit var dataBasePassword: String

    @NotBlank
    @Value("\${gnirps.database.driver-class-name}")
    lateinit var dataBaseDriverClassName: String

    @NotBlank
    @Value("\${gnirps.database.dialect}")
    lateinit var dataBaseDialect: String

    @Value("\${gnirps.database.hbm2ddl-auto}")
    @Pattern(regexp = "^(create|create/drop|validate|update)$")
    lateinit var hbm2ddlAuto: String

    @NotBlank
    @Value("\${gnirps.database.use-sql-comments}")
    lateinit var useSqlCommments: String

    @NotBlank
    @Value("\${gnirps.database.default-schema}")
    lateinit var defaultSchema: String

    @Value("\${gnirps.database.naming-strategy}")
    @Pattern(
            regexp = "^(org.hibernate.boot.model.naming" +
                    ".PhysicalNamingStrategyStandardImpl|" +
                    "com.gnirps.database.config" +
                    ".CustomPhysicalNamingStrategy" +
                    ")$"
    )
    lateinit var namingStrategy: String
}