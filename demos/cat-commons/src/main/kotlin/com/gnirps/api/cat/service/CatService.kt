package com.gnirps.api.cat.service

import com.gnirps.api.cat.mapper.CatMapper
import com.gnirps.api.cat.model.Cat
import com.gnirps.api.cat.repository.CatRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@Service
class CatService(
        private val catRepository: CatRepository
) {
    fun findAll(pageable: Pageable): Page<Cat> {
        return catRepository.findAll(pageable)
    }

    fun findById(id: UUID): Cat {
        val cat = catRepository.findById(id)
        if (!cat.isPresent) throw EntityNotFoundException("cat $id not found") else return cat.get()
    }

    fun create(cat: Cat): Cat {
        if (catRepository.existsById(cat.id)) {
            throw EntityExistsException("cat ${cat.id} already exists")
        }
        return catRepository.saveAndFlush(cat)
    }

    fun deleteById(id: UUID): Cat {
        val cat = catRepository.findById(id).orElseThrow { throw EntityNotFoundException("cat $id not found") }
        catRepository.deleteById(id)
        return cat
    }

    fun randomCat(): Cat {
        return Cat(
                id = UUID.randomUUID(),
                birthday = ZonedDateTime
                        .now()
                        .minusHours(Random().nextInt(3000).toLong())
                        .format(CatMapper.dateTimeFormatter),
                name = listOf("Schroedinger", "Rex", "Bubulle").random(),
                state = Cat.State.values().random(),
                happinessLevel = Cat.HappinessLevel.values().random()
        )
    }
}
