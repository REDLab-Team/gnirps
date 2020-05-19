package com.gnirps.commons.logging.service

interface Logger {
    enum class EventType(name: String) {
        ERROR("ERROR"),
        STACK_TRACE("STACK_TRACE"),
        OPERATION("OPERATION"),
        HTTP_REQUEST("HTTP_REQUEST"),
        HTTP_RESPONSE("HTTP_RESPONSE"),
        MISSING("")
    }

    fun trace(content: Any? = null, eventType: EventType? = null)
    fun debug(content: Any? = null, eventType: EventType? = null)
    fun info(content: Any? = null, eventType: EventType? = null)
    fun warn(content: Any? = null, eventType: EventType? = null)
    fun error(content: Any? = null, eventType: EventType? = EventType.ERROR)

    fun formatMessage(content: Any? = null, eventType: EventType? = null): String
    fun getCleanStack(throwable: Throwable): String
    fun printCleanStack(throwable: Throwable)
}
