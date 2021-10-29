package com.gnirps.logging.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.gnirps.logging.model.ExceptionMessage
import java.io.IOException

class ExceptionMessageDeserializer
@JvmOverloads constructor(
    vc: Class<*>? = null
) : StdDeserializer<ExceptionMessage?>(vc) {
    /**
     * Deserialize the JSON auth token from Keycloak
     * @param jp [JsonParser]
     * @param ctxt [DeserializationContext]
     * @return An [AuthToken] entity
     */
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): AuthToken {
        val keycloakProperties = ctxt.findInjectableValue(
            "keycloakProperties",
            null,
            null
        ) as KeycloakProperties
        val keycloakBackClientId = keycloakProperties.resource
        val authTokenNode: JsonNode = jp.codec.readTree(jp)

        // Parse user's roles
        val allRoles = ObjectMapper().readValue(
            authTokenNode.get("code").get(keycloakBackClientId).get("roles").toString(),
            Array<String>::class.java
        )

        return ExceptionMessage(
        )
    }
}
