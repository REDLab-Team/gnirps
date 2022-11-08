package com.gnirps.keycloak.config

import com.gnirps.keycloak.config.properties.KeycloakProperties
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.util.matcher.AndRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@ComponentScan(basePackageClasses = [KeycloakSecurityComponents::class])
@ConfigurationPropertiesScan(basePackageClasses = [KeycloakProperties::class])
class KeycloakConfigurerAdapter(
    private val keycloakProperties: KeycloakProperties,
    private val keycloakClientRequestFactory: KeycloakClientRequestFactory,
    private val authenticationFailureHandler: CustomAuthenticationFailureHandler,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
) : KeycloakWebSecurityConfigurerAdapter() {
    // handle the prefix ROLE_
    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider: KeycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun keycloakRestTemplate(): KeycloakRestTemplate {
        return KeycloakRestTemplate(keycloakClientRequestFactory)
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    /**
     * Spring-security déconseille d'utiliser le WebSecurity.ignoring pour échapper la sécurité sur certains URL, car
     * cela ne les fait pas passer dans Spring-security. Il vaut mieux utiliser HttpSecurity.anyMatcher().permitAll()
     * qui est un laissez-passer a posteriori et donc passe quand même dans les Spring-security
     */

    /**
     * Ignore authentication for some urls at the "Keycloak" level
     */
    @Bean
    @Throws(java.lang.Exception::class)
    override fun keycloakAuthenticationProcessingFilter(): KeycloakAuthenticationProcessingFilter? {

        val excludedRoutes = mutableListOf<NegatedRequestMatcher>()
        for (url in keycloakProperties.resources) {
            excludedRoutes.add(NegatedRequestMatcher(AntPathRequestMatcher(url, HttpMethod.GET.toString())))
        }
        for (url in keycloakProperties.endpoints.unsecureGet) {
            excludedRoutes.add(NegatedRequestMatcher(AntPathRequestMatcher(url, HttpMethod.GET.toString())))
        }
        for (url in keycloakProperties.endpoints.unsecurePost) {
            excludedRoutes.add(NegatedRequestMatcher(AntPathRequestMatcher(url, HttpMethod.POST.toString())))
        }
        for (url in keycloakProperties.endpoints.unsecurePut) {
            excludedRoutes.add(NegatedRequestMatcher(AntPathRequestMatcher(url, HttpMethod.PUT.toString())))
        }
        for (url in keycloakProperties.endpoints.unsecureDelete) {
            excludedRoutes.add(NegatedRequestMatcher(AntPathRequestMatcher(url, HttpMethod.DELETE.toString())))
        }

        val filter = KeycloakAuthenticationProcessingFilter(
            authenticationManagerBean(),
            AndRequestMatcher(
                KeycloakAuthenticationProcessingFilter.DEFAULT_REQUEST_MATCHER,
                *excludedRoutes.toTypedArray()
            )
        )
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy())
        return filter
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http
            .exceptionHandling()
            .accessDeniedHandler(customAccessDeniedHandler)
            .and().cors()
            .and().csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(*keycloakProperties.resources.toTypedArray()).permitAll()
            .antMatchers(*keycloakProperties.endpoints.unsecureGet.toTypedArray()).permitAll()
            .antMatchers(*keycloakProperties.endpoints.unsecurePost.toTypedArray()).permitAll()
            .antMatchers(*keycloakProperties.endpoints.unsecurePut.toTypedArray()).permitAll()
            .antMatchers(*keycloakProperties.endpoints.unsecureDelete.toTypedArray()).permitAll()
            .anyRequest().authenticated()
            .and().formLogin().failureHandler(authenticationFailureHandler)
    }
}
