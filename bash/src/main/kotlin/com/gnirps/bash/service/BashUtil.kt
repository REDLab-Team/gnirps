package com.gnirps.bash.service

import com.gnirps.bash.model.Script
import com.gnirps.logging.config.defaultLogger
import com.gnirps.logging.exceptions.BashException
import com.gnirps.logging.service.Logger
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class BashUtil {
    companion object {
        private val logger: Logger = defaultLogger()

        fun sequential(stopOnError: Boolean = true, vararg scripts: Script): List<Script> {
            scripts.forEach {
                try {
                    it.run()
                    logger.debug(it, Logger.EventType.OPERATION)
                } catch (bashException: BashException) {
                    logger.error(bashException)
                    if (stopOnError) return emptyList()
                }
            }
            return scripts.toList()
        }

        fun parallel(vararg scripts: Script): List<Script> {
            return runBlocking {
                scripts.toList().parallelStream().map {
                    try {
                        it.run()
                        logger.debug(it, Logger.EventType.OPERATION)
                    } catch (bashException: BashException) {
                        logger.error(bashException)
                    }
                    it
                }
            }.toList()
        }
    }
}