package com.gnirps.server.keycloak.config

import com.gnirps.logging.config.defaultLogger
import com.gnirps.logging.service.Logger
import com.gnirps.server.keycloak.config.properties.KeycloakServerProperties
import org.keycloak.Config
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.services.managers.ApplianceBootstrap
import org.keycloak.services.managers.RealmManager
import org.keycloak.services.resources.KeycloakApplication
import org.keycloak.util.JsonSerialization
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.util.*


class EmbeddedKeycloakApplication : KeycloakApplication() {
    companion object {
        const val DEFAULT_MASTER_REALM_ADMIN_USER: String = "admin"
        const val DEFAULT_MASTER_REALM_ADMIN_PASSWORD: String = "admin"

        val LOGGER: Logger = defaultLogger()

        var keycloakServerProperties: KeycloakServerProperties? = null
    }

    init {
        createMasterRealmUser()
        initOtherRealms()
    }

    override fun loadConfig() {
        Config.init(
                RegularJsonConfigProviderFactory().create().orElseThrow {
                    NoSuchElementException("missing embedded keycloak server's json configuration file")
                }
        )
    }

    private fun createMasterRealmUser() {
        val session = getSessionFactory().create()
        val applianceBootstrap = ApplianceBootstrap(session)

        try {
            val adminUsername: String = keycloakServerProperties
                    ?.masterRealm?.adminUsername?.let {
                        if (it.isNotEmpty()) it else DEFAULT_MASTER_REALM_ADMIN_USER
                    } ?: DEFAULT_MASTER_REALM_ADMIN_USER
            val adminPassword: String = keycloakServerProperties
                    ?.masterRealm?.adminPassword?.let {
                        if (it.isNotEmpty()) it else DEFAULT_MASTER_REALM_ADMIN_PASSWORD
                    } ?: DEFAULT_MASTER_REALM_ADMIN_PASSWORD

            session.transactionManager.begin()
            applianceBootstrap.createMasterRealmUser(adminUsername, adminPassword)
            session.transactionManager.commit()

        } catch (ex: Exception) {
            LOGGER.warn("could not create master realm's admin: ${ex.message}")
            session.transactionManager.rollback()
        }

        session.close()
    }

    private fun initOtherRealms() {
        val session = getSessionFactory().create()

        try {
            session.transactionManager.begin()
            val manager = RealmManager(session)

            for (filename in keycloakServerProperties?.realmsFromJson ?: emptyList()) {
                val lessonRealmImportFile: Resource = ClassPathResource(filename)
                manager.importRealm(
                        JsonSerialization.readValue(
                                lessonRealmImportFile.inputStream,
                                RealmRepresentation::class.java
                        )
                )
            }
            session.transactionManager.commit()
        } catch (ex: Exception) {
            LOGGER.warn("could not import realm from json file ${ex.message}")
            session.transactionManager.rollback()
        }

        session.close()
    }
}