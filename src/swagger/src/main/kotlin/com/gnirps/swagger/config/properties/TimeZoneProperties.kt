package com.gnirps.swagger.config.properties

import com.gnirps.commons.config.properties.CustomPropertySources
import org.springframework.beans.factory.annotation.Value

@CustomPropertySources
class TimeZoneProperties {
    @Value("\${gnirps.common.time-zone}")
    lateinit var timeZone: String
}
