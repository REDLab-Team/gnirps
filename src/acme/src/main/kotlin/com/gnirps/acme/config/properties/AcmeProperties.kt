package com.gnirps.acme.config.properties

import com.gnirps.commons.config.properties.YamlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Email

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Configuration
@PropertySources(
    PropertySource(
        "application-acme.yml",
        factory = YamlPropertySourceFactory::class
    ),
    PropertySource(
        "application.yml",
        ignoreResourceNotFound = true,
        factory = YamlPropertySourceFactory::class
    )
)
@Validated
class AcmeProperties {
    companion object {
        const val letsEncryptEndpoint: String = "acme://letsencrypt.org"
    }

    @Value("\${gnirps.acme.domain.name}")
    @NotBlank(message = "missing gnirps.acme.domain.name")
    lateinit var domainName: String

    @Value("\${gnirps.acme.contact-mail}")
    @Email
    lateinit var contactMail: String

    @Value("\${gnirps.acme.organisation}")
    @NotBlank(message = "missing gnirps.acme.organisation")
    lateinit var organisation: String

    @Value("\${gnirps.acme.account-login-url}")
    lateinit var accountLoginUrl: String

    @Value("\${gnirps.acme.keys.user}")
    @NotBlank(message = "missing gnirps.acme.keys.user")
    lateinit var userKeyFile: String

    @Value("\${gnirps.acme.keys.domain}")
    @NotBlank(message = "missing gnirps.acme.keys.domain")
    lateinit var domainKeyFile: String

    @Value("\${gnirps.acme.domain.csr-path}")
    @NotBlank(message = "missing gnirps.acme.domain.csr-path")
    lateinit var domainCsrFile: String

    @Value("\${gnirps.acme.domain.crt-path}")
    @NotBlank(message = "missing gnirps.acme.domain.crt-path")
    lateinit var domainChainFile: String

    @Value("\${gnirps.acme.keystore.file-path}")
    @NotBlank(message = "missing gnirps.acme.keytore.file-path")
    lateinit var keyStoreFile: String

    @Value("\${gnirps.acme.keystore.password}")
    @NotBlank(message = "missing gnirps.acme.keystore.password")
    lateinit var keyStorePassword: String

    @Value("\${gnirps.acme.endpoint}")
    @Pattern(
        regexp = "^(production|staging)$",
        message = "can be either 'production' or 'staging' for Let's Encrypt"
    )
    lateinit var environment: String

    @Value("\${gnirps.acme.terms-of-service-agreed}")
    @Pattern(regexp = "^(true|false)$", message = "boolean value required")
    lateinit var termsOfServiceAgreed: String
}
