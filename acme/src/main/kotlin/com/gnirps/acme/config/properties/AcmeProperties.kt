package com.gnirps.acme.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@ConstructorBinding
@ConfigurationProperties(prefix = "gnirps.acme")
@Validated
data class AcmeProperties(
        @Pattern(regexp = "^(true|false)$", message = "boolean value required")
        val termsOfServiceAgreed: String,
        @Pattern(
                regexp = "^(production|staging)$",
                message = "can be either 'production' or 'staging' for Let's Encrypt"
        )
        val endpoint: String,
        @NotBlank(message = "missing gnirps.acme.organisation")
        val organisation: String,
        @Email
        val contactMail: String,
        val accountLoginUrl: String,
        val keys: Keys,
        val domain: Domain,
        val keystore: Keystore
) {
    companion object {
        const val letsEncryptEndpoint: String = "acme://letsencrypt.org"
    }

    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.acme.keys")
    data class Keys(
            @NotBlank(message = "missing gnirps.acme.keys.user")
            val user: String,
            @NotBlank(message = "missing gnirps.acme.keys.domain")
            val domain: String
    )

    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.acme.domain")
    @Validated
    data class Domain(
            @NotBlank(message = "missing gnirps.acme.domain.name")
            val name: String,
            @NotBlank(message = "missing gnirps.acme.domain.csr-path")
            val csrPath: String,
            @NotBlank(message = "missing gnirps.acme.domain.crt-path")
            val crtPath: String
    )

    @ConstructorBinding
    @ConfigurationProperties(prefix = "gnirps.acme.keystore")
    @Validated
    data class Keystore(
            @NotBlank(message = "missing gnirps.acme.keytore.file-path")
            val filePath: String,
            @NotBlank(message = "missing gnirps.acme.keystore.password")
            val password: String
    )
}