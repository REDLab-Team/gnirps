package com.gnirps.logging.config

import com.gnirps.logging.service.Logger
import com.gnirps.logging.service.WrappedSlf4jLogger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class LoggerBeanFactory {
    @Bean("injectedSlf4jLogger")
    @Scope("prototype")
    fun logger(injectionPoint: InjectionPoint): Logger =
            WrappedSlf4jLogger(LoggerFactory.getLogger(
                    injectionPoint.methodParameter?.containingClass?.name  //constructor
                            ?: injectionPoint.field?.declaringClass?.name        // field
                            ?: injectionPoint.declaredType.name
            ))
}

inline fun <reified T : Any> T.defaultLogger(): Logger {
    return slf4jLogger()
}

inline fun <reified T : Any> T.slf4jLogger(): Logger {
    this::class.let {
        val logger = LoggerFactory.getLogger(
                if (it.isCompanion)
                    it.java.enclosingClass.name
                else
                    it.java.name
        )
        return WrappedSlf4jLogger(logger)
    }
}