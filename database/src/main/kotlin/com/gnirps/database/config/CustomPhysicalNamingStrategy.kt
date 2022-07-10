package com.gnirps.database.config

import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategy
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment
import java.util.Locale


class CustomPhysicalNamingStrategy : PhysicalNamingStrategy {
    override fun toPhysicalCatalogName(
            identifier: Identifier,
            jdbcEnv: JdbcEnvironment
    ): Identifier {
        return convertToSnakeCase(identifier)
    }

    override fun toPhysicalColumnName(
            identifier: Identifier,
            jdbcEnv: JdbcEnvironment
    ): Identifier {
        return convertToSnakeCase(identifier)
    }

    override fun toPhysicalSchemaName(
            identifier: Identifier,
            jdbcEnv: JdbcEnvironment
    ): Identifier {
        return convertToSnakeCase(identifier)
    }

    override fun toPhysicalSequenceName(
            identifier: Identifier,
            jdbcEnv: JdbcEnvironment
    ): Identifier {
        return convertToSnakeCase(identifier)
    }

    override fun toPhysicalTableName(
            identifier: Identifier,
            jdbcEnv: JdbcEnvironment
    ): Identifier {
        return convertToSnakeCase(identifier)
    }

    private fun convertToSnakeCase(identifier: Identifier): Identifier {
        val regex = "([a-z])([A-Z])"
        val replacement = "$1_$2"
        val newName = identifier
            .text
            .replace(regex, replacement)
            .lowercase(Locale.getDefault())
        return Identifier.toIdentifier(newName)
    }
}
