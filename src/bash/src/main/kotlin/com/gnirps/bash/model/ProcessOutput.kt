package com.gnirps.bash.model

enum class ProcessOutput(val value: ProcessBuilder.Redirect) {
    STDOUT(ProcessBuilder.Redirect.INHERIT),
    STRING(ProcessBuilder.Redirect.PIPE)
}