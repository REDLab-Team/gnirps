package com.gnirps.commons.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException


fun String.isValidJson(): Boolean {
    return try {
        val mapper = ObjectMapper()
        mapper.readTree(this)
        true
    } catch (e: IOException) {
        false
    }
}
