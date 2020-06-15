package com.gnirps.acme.service

import org.springframework.stereotype.Component
import javax.persistence.EntityNotFoundException

@Component
class ChallengeStore {
    private val challenges: HashMap<String, String> = hashMapOf()

    @Throws(EntityNotFoundException::class)
    fun get(token: String): String {
        return challenges[token]
                ?: throw EntityNotFoundException("challenge $token not found")
    }

    fun put(token: String, authorization: String) {
        challenges[token] = authorization
    }
}
