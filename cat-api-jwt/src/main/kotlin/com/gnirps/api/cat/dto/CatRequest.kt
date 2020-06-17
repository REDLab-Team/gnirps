package com.gnirps.api.catjwt.dto

import com.gnirps.api.catjwt.model.Cat

data class CatRequest (
        val birthday: String? = null,
        val name: String = "",
        val happinessLevel: Cat.HappinessLevel = Cat.HappinessLevel.UNDEFINED,
        val state: Cat.State = Cat.State.UNDEFINED
)
