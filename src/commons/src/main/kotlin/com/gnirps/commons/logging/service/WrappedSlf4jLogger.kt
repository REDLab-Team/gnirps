package com.gnirps.commons.logging.service

class WrappedSlf4jLogger(private val logger: org.slf4j.Logger): AbstractLogger() {
    override fun trace(content: Any?, eventType: Logger.EventType?) {
        logger.trace(formatMessage(content = content, eventType = eventType))
    }

    override fun debug(content: Any?, eventType: Logger.EventType?) {
        logger.debug(formatMessage(content = content, eventType = eventType))
    }

    override fun info(content: Any?, eventType: Logger.EventType?) {
        logger.info(formatMessage(content = content, eventType = eventType))
    }

    override fun warn(content: Any?, eventType: Logger.EventType?) {
        logger.warn(formatMessage(content = content, eventType = eventType))
    }

    override fun error(content: Any?, eventType: Logger.EventType?) {
        logger.error(formatMessage(content = content, eventType = eventType))
    }
}
