package com.gnirps.database.postgresql.config

import com.gnirps.database.postgresql.config.properties.PostgresqlProperties
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = [JpaConfiguration.PACKAGES_TO_SCAN],
    repositoryImplementationPostfix = "Repository",
    considerNestedRepositories = true
)
@EntityScan(
    basePackages = [JpaConfiguration.PACKAGES_TO_SCAN]
)
@EnableTransactionManagement
class JpaConfiguration(val postgresqlProperties: PostgresqlProperties) {
    companion object {
        const val PACKAGES_TO_SCAN: String = "com.gnirps"
    }
    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setDatabase(Database.POSTGRESQL)
        vendorAdapter.setGenerateDdl(true)

        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource()
        em.jpaVendorAdapter = vendorAdapter
        em.setPackagesToScan(PACKAGES_TO_SCAN)
        em.setJpaProperties(additionalProperties())

        return em
    }

    @Bean
    fun dataSource(): DataSource {
        val dataSourceConfig = HikariConfig()
        dataSourceConfig.driverClassName = postgresqlProperties.driverClassName
        dataSourceConfig.jdbcUrl = postgresqlProperties.url
        dataSourceConfig.username = postgresqlProperties.username
        dataSourceConfig.password = postgresqlProperties.password
        return HikariDataSource(dataSourceConfig)
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): JpaTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        return transactionManager
    }

    private fun additionalProperties(): Properties {
        Properties().let {
            it.putAll(
                mapOf(
                    Pair(
                        "hibernate.dialect",
                        postgresqlProperties.databaseDialect
                    ), Pair(
                        "hibernate.hbm2ddl.auto",
                        postgresqlProperties.hbm2ddlAuto
                    ), Pair(
                        "hibernate.use_sql_comments",
                        postgresqlProperties.useSqlCommments
                    ), Pair(
                        "hibernate.default_schema",
                        postgresqlProperties.defaultSchema
                    ), Pair(
                        "hibernate.physical_naming_strategy",
                        postgresqlProperties.namingStrategy
                    ), Pair(
                        "hibernate.temp.use_jdbc_metadata_defaults",
                        "false"
                    ), Pair(
                        "hibernate.hbm2dll.create_namespaces",
                        true
                    )
                )
            )
            return it
        }
    }
}
