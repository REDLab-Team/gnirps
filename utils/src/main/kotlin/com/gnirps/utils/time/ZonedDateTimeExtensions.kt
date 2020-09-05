package com.gnirps.utils.time

import com.gnirps.utils.time.DatetimeFormats.Companion.ISO8601_OFFSET_DATE_TIME_HHMM
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun ZonedDateTime.toDate(timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    return Date(this.withZoneSameInstant(timeZone.toZoneId()).toInstant().toEpochMilli())
}

fun ZonedDateTime.toTimestamp(timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Timestamp {
    return Timestamp(this.withZoneSameInstant(timeZone.toZoneId()).toInstant().toEpochMilli())
}

fun String.toZonedDateTime(dateFormat: String = ISO8601_OFFSET_DATE_TIME_HHMM, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): ZonedDateTime {
    return this.toDate(dateFormat, timeZone).toZonedDateTime(timeZone)
}

fun Long.toZonedDateTime(timeZone: TimeZone = TimeZone.getTimeZone("UTC")): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), timeZone.toZoneId())
}

fun ZonedDateTime.formatTo(dateFormat: String = ISO8601_OFFSET_DATE_TIME_HHMM): String {
    val formatter = DateTimeFormatter.ofPattern(dateFormat)
    return this.format(formatter)
}