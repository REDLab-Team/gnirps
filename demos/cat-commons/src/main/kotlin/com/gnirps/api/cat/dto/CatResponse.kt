package com.gnirps.api.cat.dto

import com.gnirps.api.cat.model.Cat
import java.time.ZonedDateTime
import java.util.*

data class CatResponse(
        val id: UUID,
        val birthday: ZonedDateTime,
        val name: String,
        val happinessLevel: Cat.HappinessLevel,
        val state: Cat.State
)
