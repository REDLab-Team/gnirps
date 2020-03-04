package com.gnirps.bash.service

import com.gnirps.bash.model.ProcessOutput
import com.gnirps.commons.logging.service.Logger
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Component
class BashExec(private val logger: Logger) {
    fun run(
        cmd: String,
        workDir: String = "/",
        output: ProcessOutput = ProcessOutput.STDOUT,
        error: ProcessOutput = ProcessOutput.STDOUT,
        timeout: Long = 300,
        inputStream: String? = null
    ): Process {
        val process = Process(
                cmd = cmd,
                workDir = workDir,
                output = output,
                error = error,
                timeout = timeout,
                inputStream = inputStream
        )
        return run(process)
    }

    fun run(process: Process): Process {
        logger.info("run: '${process.cmd}'", Logger.EventType.OPERATION)
        process.run()
        if (process.exitValue != 0) {
            logger.error(process.summary())
        } else {
            logger.info(process.summary(), Logger.EventType.OPERATION)
        }
        return process
    }

    fun sequential(stopOnError: Boolean = true, vararg processes: Process): List<Process> {
        processes.forEach {
            logger.info("run: '${it.cmd}'", Logger.EventType.OPERATION)
            it.run()
            if (it.exitValue != 0) {
                logger.error("interrupt: ${it.summary()}")
                return emptyList()
            }
            logger.info(it.summary(), Logger.EventType.OPERATION)
        }
        return processes.toList()
    }

    fun parallel(vararg processes: Process): List<Process> {
        return runBlocking {
            processes.toList().parallelStream().map {
                logger.info("run: '${it.cmd}'", Logger.EventType.OPERATION)
                it.run()
                if (it.exitValue != 0) {
                    logger.error(it.summary())
                } else {
                    logger.info(it.summary(), Logger.EventType.OPERATION)
                }
                it
            }
        }.toList()
    }
}