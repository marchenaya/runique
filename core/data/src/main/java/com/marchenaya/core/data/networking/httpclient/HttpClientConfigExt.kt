package com.marchenaya.core.data.networking.httpclient

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val X_API_KEY = "x-api-key"

fun HttpClientConfig<*>.applyDefaultConfiguration(
    apiKey: String,
    serializeJson: Boolean = true
) {
    if (serializeJson) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        installSecureLogging()
    }
    defaultRequest {
        if (serializeJson) {
            contentType(ContentType.Application.Json)
        }
        header(X_API_KEY, apiKey)
    }
}

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
                    header.equals(X_API_KEY, ignoreCase = true)
        }
    }
}