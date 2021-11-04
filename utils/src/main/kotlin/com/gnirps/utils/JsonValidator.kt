package com.gnirps.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun String.isJsonObject(): Boolean =
    try {
        JSONObject(this)
        true
    } catch (je: JSONException) {
        false
    }

fun String.isJsonArray(): Boolean =
    try {
        JSONArray(this)
        true
    } catch (je: JSONException) {
        false
    }

fun String.isValidJson(): Boolean =
    this.isJsonObject() || this.isJsonArray()
