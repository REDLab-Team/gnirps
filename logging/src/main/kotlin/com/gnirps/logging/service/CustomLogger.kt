package com.gnirps.logging.service

interface CustomLogger : Logger {
    var logger: org.slf4j.Logger
}
