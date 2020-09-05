package com.gnirps.api.cat.repository

import com.gnirps.api.cat.model.Cat
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CatRepository : JpaRepository<Cat, UUID> {
    override fun findAll(pageable: Pageable): Page<Cat>
}
