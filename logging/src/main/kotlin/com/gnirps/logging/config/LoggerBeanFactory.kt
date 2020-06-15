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
    fun logger(injectionPoint: InjectionPoint): Logger {
        val clazz = injectionPoint.methodParameter?.containingClass//constructor
                ?: injectionPoint.field?.declaringClass            // field
        return WrappedSlf4jLogger(LoggerFactory.getLogger(clazz?.simpleName))
    }
}

inline fun <reified T : Any> T.defaultLogger(): Logger {
    return slf4jLogger()
}

inline fun <reified T : Any> T.slf4jLogger(): Logger {
    this::class.let {
        val logger = LoggerFactory.getLogger(
                if (it.isCompanion)
                    it.java.enclosingClass.simpleName
                else
                    it.java.simpleName
        )
        return WrappedSlf4jLogger(logger)
    }
}