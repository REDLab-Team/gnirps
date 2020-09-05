package com.gnirps.server.keycloak

import com.gnirps.core.SpringBootEntryPoint
import com.gnirps.core.config.properties.TimeZoneProperties
import com.gnirps.logging.service.Logger
import com.gnirps.server.keycloak.config.properties.KeycloakServerProperties
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.*
import javax.annotation.PostConstruct


@SpringBootApplication(
        scanBasePackages = ["com.gnirps"],
        exclude = [LiquibaseAutoConfiguration::class, SpringBootEntryPoint::class]
)
@EnableJpaRepositories(basePackages = ["com.gnirps"])
@ConfigurationPropertiesScan
class SpringBootEntryPoint(
        private val timeZoneProperties: TimeZoneProperties,
        private val logger: Logger
) {
    companion object {
        var context: ConfigurableApplicationContext? = null

        @JvmStatic
        fun restart() {
            val args = context!!.getBean(ApplicationArguments::class.java)

            val thread = Thread {
                context!!.close()
                context = SpringApplication.run(
                        SpringBootEntryPoint::class.java,
                        *args.sourceArgs
                )
            }

            thread.isDaemon = false
            thread.start()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            context = runApplication<SpringBootEntryPoint>(*args)
        }
    }


    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneProperties.name))
    }

    @Bean
    fun onApplicationReadyEventListener(
            serverProperties: ServerProperties,
            keycloakServerProperties: KeycloakServerProperties
    ): ApplicationListener<ApplicationReadyEvent> {
        return ApplicationListener {
            logger.info(
                    "Embedded Keycloak started: " +
                            "http://localhost:${serverProperties.port}${keycloakServerProperties.contextPath} " +
                            "to use keycloak"
            )
        }
    }
}