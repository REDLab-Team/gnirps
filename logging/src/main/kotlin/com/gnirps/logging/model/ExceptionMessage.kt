package com.gnirps.logging.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.gnirps.logging.service.ExceptionMessageDeserializer

/**
 * ExceptionMessage Object user for HttpException
 */
@JsonDeserialize(using = ExceptionMageDeserializer::class)
data class ExceptionMessage(
    val code : String,
    val status : String,
    val body : Any
)
