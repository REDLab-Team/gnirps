package com.gnirps.bash.service

import com.gnirps.bash.model.ProcessOutput
import com.gnirps.commons.exceptions.BashException
import com.gnirps.commons.utils.measureTimeMillis
import java.io.*
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

class Process(
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
    @Throws(BashException::class, IOException::class)
    fun run() {
        val processBuilder = ProcessBuilder(cmd.split(" "))
                .directory(File(workDir))
                .redirectOutput(output.value)
                .redirectError(error.value)

        val time = measureTimeMillis {
            val proc = processBuilder.start()
            BufferedWriter(OutputStreamWriter(proc.outputStream)).write(inputStream?: "")

            if (!proc.waitFor(timeout, TimeUnit.SECONDS))
                throw BashException(exitCode = 124, message = "script '$cmd' timed out")

            pid = proc.pid()
            exitValue = proc.exitValue()

            if (output == ProcessOutput.STRING) outputString = proc.inputStream.bufferedReader().readText()
            if (error == ProcessOutput.STRING) errorString = proc.errorStream.bufferedReader().readText()
        }
        execTime = time

        if (exitValue != 0) {
            val message: String = if (errorString.isNotEmpty()) errorString else ""
            throw BashException(exitCode = exitValue, message = message)
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