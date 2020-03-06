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
    fun run() {
        val processBuilder = ProcessBuilder(cmd.split(" "))
                .directory(File(workDir))
                .redirectOutput(output.value)
                .redirectError(error.value)
        val time = measureTimeMillis {
            val proc = processBuilder.start()
            BufferedWriter(OutputStreamWriter(proc.outputStream)).write(inputStream?: "")

            proc.waitFor(timeout, TimeUnit.SECONDS)

            pid = proc.pid()
            exitValue = proc.exitValue()

            try {
                if (output == ProcessOutput.STRING) {
                    outputString = proc.inputStream.bufferedReader().readText()
                }
                if (error == ProcessOutput.STRING) {
                    errorString = proc.errorStream.bufferedReader().readText()
                }
            } catch (ioe: IOException) {
                throw ioe
            }
        }
        execTime=time
        if (exitValue != 0) {
            val message: String = if (errorString.isNotEmpty()) errorString else ""
            throw BashException(exitCode = exitValue, message = message)
        }
    }

    fun summary(): String {
        val outputField: String =
                if (outputString.isNotEmpty())
                    "output_string: '${this.outputString}'"
                else
                    ""
        val errorField: String =
                if (errorString.isNotEmpty())
                    "error_string: '${this.errorString}'"
                else
                    ""
        val fields = listOf(
                "pid: $pid",
                "exitValue: $exitValue",
                "workDir: '$workDir'",
                "cmd: '$cmd'",
                "execTime: '$execTime'",
                outputField,
                errorField
        )
                .filter { it.isNotEmpty() }
                .joinToString()
        return "{bashScript: {$fields}}"
    }
}