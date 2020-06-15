package com.gnirps.bash.model

import com.gnirps.logging.exceptions.BashException
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class Script(
        val cmd: String,
        val workDir: String = "/",
        val output: ProcessOutput = ProcessOutput.STDOUT,
        val error: ProcessOutput = ProcessOutput.STDOUT,
        val timeout: Long = 300,
        val inputStream: String? = null,
        var exitValue: Int = -1,
        var pid: Long = -1,
        var outputString: String = "",
        var errorString: String = "",
        var execTime: Long? = null
) {
    enum class ProcessOutput(val value: ProcessBuilder.Redirect) {
        STDOUT(ProcessBuilder.Redirect.INHERIT),
        STRING(ProcessBuilder.Redirect.PIPE)
    }

    @Throws(BashException::class, IOException::class)
    fun run(): Script {
        val processBuilder = ProcessBuilder(cmd.split(" "))
                .directory(File(workDir))
                .redirectOutput(output.value)
                .redirectError(error.value)

        val time = measureTimeMillis {
            try {
                val proc = processBuilder.start()
                BufferedWriter(OutputStreamWriter(proc.outputStream)).write(inputStream ?: "")

                if (!proc.waitFor(timeout, TimeUnit.SECONDS))
                    throw BashException(exitCode = 124, message = "script '$cmd' timed out")

                pid = proc.pid()
                exitValue = proc.exitValue()

                if (output == ProcessOutput.STRING) outputString = proc.inputStream.bufferedReader().readText()
                if (error == ProcessOutput.STRING) errorString = proc.errorStream.bufferedReader().readText()
            } catch (_: IOException) {
                throw BashException(exitCode = 127, message = "command not found: $workDir$cmd")
            }
        }
        execTime = time

        when (exitValue) {
            0 -> return this
            124 -> throw BashException(exitCode = exitValue, message = "process timed out: $pid")
            127 -> throw BashException(exitCode = exitValue, message = "command not found: $workDir$cmd")
            else -> {
                val message: String = when {
                    errorString.isNotEmpty() -> errorString
                    outputString.isNotEmpty() -> outputString
                    else -> "command did not return a 0 value"
                }
                throw BashException(exitCode = exitValue, message = message)
            }
        }
    }

    override fun toString(): String {
        return "{" +
                "\"cmd\": \"$workDir/$cmd\"" +
                if (outputString.isNotEmpty()) ", \"output\": \"$outputString\"" else "" +
                        if (errorString.isNotEmpty()) ", \"error\": \"$errorString\"" else "" +
                                ", \"exit_value\": $exitValue" +
                                ", \"exec_time\": $execTime" +
                                "}"
    }
}