package com.gnirps.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

/**
 * Whether a string is a valid JSON or not
 * @return [Boolean]
 */
fun String.isValidJson(): Boolean {
    return try {
        val mapper = ObjectMapper()
        mapper.readTree(this)
        true
    } catch (e: IOException) {
        false
    }
}
