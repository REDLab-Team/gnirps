package com.gnirps.api.catjwt.dto

import com.gnirps.api.catjwt.model.Cat
import java.time.ZonedDateTime
import java.util.*

data class CatResponse (
        val id: UUID,
        val birthday: ZonedDateTime,
        val name: String,
        val happinessLevel: Cat.HappinessLevel,
        val state: Cat.State
)
