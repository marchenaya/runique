package com.marchenaya.core.data.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import timber.log.Timber

fun HttpClientConfig<*>.installSecureLogging() {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Timber.d(SensitiveDataRedactor.redact(message))
            }
        }
        level = LogLevel.ALL
        sanitizeHeader { header ->
            header.equals(HttpHeaders.Authorization, ignoreCase = true) ||
                    header.equals("x-api-key", ignoreCase = true)
        }
    }
}