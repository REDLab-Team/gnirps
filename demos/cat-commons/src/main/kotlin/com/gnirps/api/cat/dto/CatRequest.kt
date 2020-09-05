package com.gnirps.api.cat.dto

import com.gnirps.api.cat.model.Cat

data class CatRequest(
        val birthday: String? = null,
        val name: String = "",
        val happinessLevel: Cat.HappinessLevel = Cat.HappinessLevel.UNDEFINED,
        val state: Cat.State = Cat.State.UNDEFINED
)
