package com.gnirps.database.config

import com.gnirps.commons.logging.service.Logger
import com.gnirps.database.properties.DatabaseProperties
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


/**
 * Manages the implementation of the database with JPA and Hibernate.
 */
@Configuration
@EnableJpaRepositories(basePackages = [JpaConfiguration.MODEL_PATH])
class JpaConfiguration(
        private val logger: Logger,
        private val databaseProperties: DatabaseProperties

) {
    companion object { const val MODEL_PATH = "com.gnirps" }

    @Bean
    @Primary
    fun dataSource(): DataSource? {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(databaseProperties.dataBaseDriverClassName)
        dataSource.url = databaseProperties.dataBaseUrl
        dataSource.username = databaseProperties.dataBaseUser
        dataSource.password = databaseProperties.dataBasePassword
        logger.debug(
                "datasource_url: ${databaseProperties.dataBaseUrl}, " +
                "datasource_username: ${databaseProperties.dataBaseUser}"
        )
        return dataSource
    }

    @Bean("entityManagerFactory")
    @Primary
    fun entityManagerFactoryBean(
            dataSource: DataSource,
            vendorAdapter: JpaVendorAdapter?
    ): LocalContainerEntityManagerFactoryBean? {
        val properties = HashMap<String, Any?>()
        properties["hibernate.hbm2ddl.auto"] = "update"
        properties["hibernate.dialect"] = databaseProperties.dataBaseDialect
        properties["hibernate.implicit_naming_strategy"] = databaseProperties.namingStrategy
        properties["hibernate.physical_naming_strategy"] = databaseProperties.namingStrategy
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        entityManagerFactoryBean.dataSource = dataSource
        entityManagerFactoryBean.jpaVendorAdapter = vendorAdapter
        entityManagerFactoryBean.setPackagesToScan(MODEL_PATH)
        entityManagerFactoryBean.setJpaPropertyMap(properties)
        entityManagerFactoryBean.afterPropertiesSet()
        return entityManagerFactoryBean
    }

    @Bean
    @Primary
    fun jpaVendorAdapter(): JpaVendorAdapter? {
        return HibernateJpaVendorAdapter()
    }

    @Bean
    @Primary
    fun transactionManager(
            entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager? {
        return JpaTransactionManager(entityManagerFactory)
    }
}