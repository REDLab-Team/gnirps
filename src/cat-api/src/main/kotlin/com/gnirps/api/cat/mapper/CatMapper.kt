package com.gnirps.api.cat.mapper

import com.gnirps.api.cat.dto.CatRequest
import com.gnirps.api.cat.dto.CatResponse
import com.gnirps.api.cat.model.Cat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CatMapper {
    companion object {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

        fun fromRequest(catRequest: CatRequest): Cat =
                Cat(
                        id = UUID.randomUUID(),
                        birthday =
                            if (catRequest.birthday != null) catRequest.birthday.format(dateTimeFormatter)
                            else ZonedDateTime.now().format(dateTimeFormatter),
                        name = catRequest.name,
                        happinessLevel = catRequest.happinessLevel,
                        state = catRequest.state
                )

        fun toResponse(cat: Cat): CatResponse =
                CatResponse(
                        id = cat.id,
                        birthday = ZonedDateTime.parse(cat.birthday, dateTimeFormatter),
                        name = cat.name,
                        happinessLevel = cat.happinessLevel,
                        state = cat.state
                )
    }
}
