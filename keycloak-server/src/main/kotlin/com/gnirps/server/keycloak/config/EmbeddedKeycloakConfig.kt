package com.gnirps.server.keycloak.config

import com.gnirps.server.keycloak.config.properties.KeycloakServerProperties
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters
//import org.keycloak.services.filters.KeycloakSessionServletFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
//import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(value = [KeycloakServerProperties::class])
class EmbeddedKeycloakConfig {
    @Bean
    fun keycloakJaxRsApplication(
            keycloakServerProperties: KeycloakServerProperties,
            dataSource: DataSource
    ): ServletRegistrationBean<HttpServlet30Dispatcher> {
        EmbeddedKeycloakApplication.keycloakServerProperties = keycloakServerProperties
        val servlet = ServletRegistrationBean(HttpServlet30Dispatcher())
        servlet.addInitParameter("javax.ws.rs.Application", EmbeddedKeycloakApplication::class.qualifiedName)
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX, keycloakServerProperties.contextPath)
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_USE_CONTAINER_FORM_PARAMS, "true")
        servlet.addUrlMappings(keycloakServerProperties.contextPath + "/*")
        servlet.setLoadOnStartup(1)
        servlet.isAsyncSupported = true
        return servlet
    }

//    @Bean
//    fun keycloakSessionManagement(keycloakServerProperties: KeycloakServerProperties): FilterRegistrationBean<KeycloakSessionServletFilter> {
//        val filter = FilterRegistrationBean<KeycloakSessionServletFilter>()
//        filter.setName("Keycloak Session Management")
//        filter.filter = KeycloakSessionServletFilter()
//        filter.addUrlPatterns(keycloakServerProperties.contextPath + "/*")
//        return filter
//    }
}