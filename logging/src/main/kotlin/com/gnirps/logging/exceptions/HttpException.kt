package com.gnirps.logging.exceptions

import org.springframework.http.HttpStatus

class HttpException(
        val type: HttpStatus.Series,
        val status: HttpStatus,
        message: String
) : Exception(message)
