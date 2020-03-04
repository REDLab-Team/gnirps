package com.gnirps.commons.logging.service

interface Logger {
    enum class EventType(name: String) {
        ERROR("ERROR"),
        OPERATION("OPERATION"),
        HTTP_REQUEST("HTTP_REQUEST"),
        HTTP_RESPONSE("HTTP_RESPONSE"),
        MISSING("")
    }

    fun trace(content: Any? = null, eventType: EventType = EventType.MISSING)
    fun debug(content: Any? = null, eventType: EventType = EventType.ERROR)
    fun info(content: Any? = null, eventType: EventType = EventType.MISSING)
    fun warn(content: Any? = null, eventType: EventType = EventType.MISSING)
    fun error(content: Any? = null, eventType: EventType = EventType.ERROR)

    fun formatMessage(content: Any? = null, eventType: EventType = EventType.MISSING): String
    fun cleanError(throwable: Throwable)
}
