package com.gnirps.utils.time

import com.gnirps.utils.time.DatetimeFormats.Companion.ISO8601_OFFSET_DATE_TIME_HHMM
import java.sql.Timestamp
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

fun Long.toDate(): Date {
    return Date(this)
}

fun Timestamp.toDate(timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    return Date(this.time + timeZone.rawOffset.toLong())
}

fun Timestamp.toZonedDateTime(timeZone: TimeZone = TimeZone.getTimeZone("UTC")): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), timeZone.toZoneId())
}

fun Timestamp.formatTo(dateFormat: String = ISO8601_OFFSET_DATE_TIME_HHMM, timeZone: TimeZone = TimeZone.getDefault()): String {
    return this.toZonedDateTime(timeZone).formatTo(dateFormat)
}