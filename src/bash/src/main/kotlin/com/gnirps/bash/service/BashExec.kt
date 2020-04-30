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
        workDir: String = "",
        output: ProcessOutput = ProcessOutput.STDOUT,
        error: ProcessOutput = ProcessOutput.STDOUT,
        timeout: Long = 300,
        inputStream: String? = null
    ): Process = run(
            Process(
                cmd = cmd,
                workDir = workDir,
                output = output,
                error = error,
                timeout = timeout,
                inputStream = inputStream
            )
    )

    fun run(process: Process): Process {
        process.run()
        if (process.exitValue != 0) {
            logger.error(process)
        } else {
            logger.debug(process, Logger.EventType.OPERATION)
        }
        return process
    }

    fun sequential(stopOnError: Boolean = true, vararg processes: Process): List<Process> {
        processes.forEach {
            it.run()
            if (it.exitValue != 0) {
                logger.error(it)
                return emptyList()
            }
            logger.debug(it, Logger.EventType.OPERATION)
        }
        return processes.toList()
    }

    fun parallel(vararg processes: Process): List<Process> {
        return runBlocking {
            processes.toList().parallelStream().map {
                it.run()
                if (it.exitValue != 0) {
                    logger.error(it)
                } else {
                    logger.trace(it, Logger.EventType.OPERATION)
                }
                it
            }
        }.toList()
    }
}