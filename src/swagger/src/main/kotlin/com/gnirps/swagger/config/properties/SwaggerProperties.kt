package com.gnirps.swagger.config.properties

import com.gnirps.commons.config.properties.CustomPropertySources
import org.springframework.beans.factory.annotation.Value

@CustomPropertySources
class SwaggerProperties {
    @Value("\${gnirps.common.base-package}")
    lateinit var basePackage: String

    @Value("\${gnirps.common.service-name}")
    lateinit var serviceName: String

    @Value("\${gnirps.swagger.api.title}")
    lateinit var apiTitle: String

    @Value("\${gnirps.swagger.api.description}")
    lateinit var apiDescription: String

    @Value("\${gnirps.swagger.api.version}")
    lateinit var apiVersion: String

    @Value("\${gnirps.swagger.api.license}")
    lateinit var apiLicense: String

    @Value("\${gnirps.swagger.api.license-url}")
    lateinit var apiLicenseUrl: String

    @Value("\${gnirps.swagger.api.terms-of-service}")
    lateinit var apiTermsOfService: String

    @Value("\${gnirps.swagger.maintainer.name}")
    lateinit var maintainerName: String

    @Value("\${gnirps.swagger.maintainer.email}")
    lateinit var maintainerEmail: String

    @Value("\${gnirps.swagger.maintainer.website}")
    lateinit var maintainerWebsite: String
}
