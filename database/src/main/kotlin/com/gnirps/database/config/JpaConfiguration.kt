package com.gnirps.database.config

import com.gnirps.database.config.properties.DatabaseProperties
import com.gnirps.logging.service.Logger
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
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
@ComponentScan("com.gnirps")
@ConfigurationPropertiesScan(basePackages = ["com.gnirps.database.config.properties"])
class JpaConfiguration(
        private val logger: Logger,
        private val properties: DatabaseProperties

) {
    companion object { const val MODEL_PATH = "com.gnirps" }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(properties.driverClassName)
        dataSource.url = properties.url
        dataSource.username = properties.username
        dataSource.password = properties.password
        logger.debug(
                "datasource_url: ${properties.url}, " +
                "datasource_username: ${properties.username}"
        )
        return dataSource
    }

    @Bean("entityManagerFactory")
    @Primary
    fun entityManagerFactoryBean(
            dataSource: DataSource,
            vendorAdapter: JpaVendorAdapter
    ): LocalContainerEntityManagerFactoryBean? {
        val jpaProperties = HashMap<String, Any>()
        jpaProperties["hibernate.hbm2ddl.auto"] = properties.hbm2ddlAuto
        jpaProperties["hibernate.dialect"] = properties.dialect
        jpaProperties["hibernate.default_schema"] = properties.defaultSchema
        jpaProperties["hibernate.use_sql_comments"] = properties.useSqlComments
        jpaProperties["hibernate.temp.use_jdbc_metadata_defaults"] = "false"
        jpaProperties["hibernate.hbm2dll.create_namespaces"] = "true"
        jpaProperties["hibernate.physical_naming_strategy"] = properties.namingStrategy
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        entityManagerFactoryBean.dataSource = dataSource
        entityManagerFactoryBean.jpaVendorAdapter = vendorAdapter
        entityManagerFactoryBean.setPackagesToScan(MODEL_PATH)
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties)
        entityManagerFactoryBean.afterPropertiesSet()
        logger.debug(jpaProperties)
        return entityManagerFactoryBean
    }

    @Bean
    @Primary
    fun jpaVendorAdapter(): JpaVendorAdapter {
        return HibernateJpaVendorAdapter()
    }

    @Bean
    @Primary
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}