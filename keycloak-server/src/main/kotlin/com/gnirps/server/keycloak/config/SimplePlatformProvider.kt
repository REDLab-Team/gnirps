package com.gnirps.server.keycloak.config

import com.gnirps.logging.config.defaultLogger
import com.gnirps.logging.service.Logger
import org.keycloak.platform.PlatformProvider
import kotlin.system.exitProcess

class SimplePlatformProvider(private var shutdownHook: Runnable? = null) : PlatformProvider {
    companion object {
        val LOGGER: Logger = defaultLogger()
    }

    override fun onStartup(startupHook: Runnable) {
        startupHook.run()
    }

    override fun onShutdown(shutdownHook: Runnable) {
        this.shutdownHook = shutdownHook
    }

    override fun exit(cause: Throwable) {
        LOGGER.error(cause)
        LOGGER.printCleanStack(cause)
        object : Thread() {
            override fun run() {
                exitProcess(1)
            }
        }.start()
    }
}
