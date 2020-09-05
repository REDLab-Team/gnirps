package com.gnirps.utils.time

import com.gnirps.utils.time.DatetimeFormats.Companion.ISO8601_OFFSET_DATE_TIME_HHMM
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

fun String.toDate(dateFormat: String = ISO8601_OFFSET_DATE_TIME_HHMM, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.toTimestamp(): Timestamp {
    return Timestamp(this.time)
}

fun Date.toZonedDateTime(timeZone: TimeZone = TimeZone.getDefault()): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), timeZone.toZoneId())
}

fun Date.formatTo(dateFormat: String = ISO8601_OFFSET_DATE_TIME_HHMM, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}