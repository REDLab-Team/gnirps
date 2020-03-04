package com.gnirps.commons.config.properties

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.io.IOException
import java.lang.IllegalStateException
import java.io.FileNotFoundException
import java.util.*


class YamlPropertySourceFactory : PropertySourceFactory {
    @Throws(IOException::class)
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val propertiesFromYaml = loadYamlIntoProperties(resource)
        val sourceName = name ?: resource.resource.filename
        if (propertiesFromYaml?.isEmpty != false)
            return PropertiesPropertySource("none", Properties())
        return PropertiesPropertySource(sourceName, propertiesFromYaml)
    }

    @Throws(FileNotFoundException::class)
    private fun loadYamlIntoProperties(resource: EncodedResource): Properties? {
        try {
            val factory = YamlPropertiesFactoryBean()
            factory.setResources(resource.resource)
            factory.afterPropertiesSet()
            return factory.getObject()
        } catch (e: IllegalStateException) {
            val cause = e.cause
            if (cause is FileNotFoundException)
            // to enforce ignoreResourceNotFound always true
                return null
            throw e
        }
    }
}