package com.gnirps.core

import com.gnirps.core.config.properties.TimeZoneProperties
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication(scanBasePackages = ["com.gnirps"])
@EnableJpaRepositories(basePackages = ["com.gnirps"])
@ConfigurationPropertiesScan
class SpringBootEntryPoint(val timeZoneProperties: TimeZoneProperties) {
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
    fun init() = TimeZone.setDefault(TimeZone.getTimeZone(timeZoneProperties.name))
}
