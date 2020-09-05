package com.gnirps.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

data class CustomPageRequest(
        val page: Int?,
        val size: Int?,
        val direction: String?,
        val properties: Array<String>
) {
    companion object {
        const val DEFAULT_PAGE: Int = 0
        const val DEFAULT_SIZE: Int = 10
        val DEFAULT_DIRECTION: Sort.Direction = Sort.DEFAULT_DIRECTION
    }

    fun toPageRequest(): PageRequest {
        return PageRequest.of(
                page ?: DEFAULT_PAGE,
                size ?: DEFAULT_SIZE,
                if (direction != null) Sort.Direction.fromString(direction) else DEFAULT_DIRECTION,
                *properties
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomPageRequest

        if (page != other.page) return false
        if (size != other.size) return false
        if (direction != other.direction) return false
        if (!properties.contentEquals(other.properties)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = page ?: 0
        result = 31 * result + (size ?: 0)
        result = 31 * result + (direction?.hashCode() ?: 0)
        result = 31 * result + properties.contentHashCode()
        return result
    }
}
