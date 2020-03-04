package com.gnirps.swagger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.ApplicationArguments

@EnableSwagger2
@SpringBootApplication(scanBasePackages = ["com.gnirps"])
class SpringBootEntryPoint {
    companion object {
        var context: ConfigurableApplicationContext? = null

        @JvmStatic fun restart() {
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

        @JvmStatic fun main(args: Array<String>) {
            context = runApplication<SpringBootEntryPoint>(*args)
        }
    }
}
