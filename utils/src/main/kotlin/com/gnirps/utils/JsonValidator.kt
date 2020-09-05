package com.gnirps.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

class JsonValidator {
    fun String.isValidJson(): Boolean {
        return try {
            val mapper = ObjectMapper()
            mapper.readTree(this)
            true
        } catch (e: IOException) {
            false
        }
    }
}