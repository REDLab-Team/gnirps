package com.gnirps.api.cat.model

import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
data class Cat(
        @Id val id: UUID,
        @DateTimeFormat val birthday: String,
        @NotBlank val name: String,
        @NotNull val happinessLevel: HappinessLevel,
        @NotNull val state: State
) {
    enum class HappinessLevel {
        DEFINITELY_NOT_HAPPY,
        NOT_SO_HAPPY,
        QUITE_HAPPY,
        VERY_HAPPY,
        THE_HAPPIEST,
        UNDEFINED
    }

    enum class State {
        ALIVE,
        DEAD_OR_ALIVE,
        DEAD,
        UNDEFINED
    }
}
