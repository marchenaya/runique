package com.marchenaya.core.data.networking

import com.marchenaya.core.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header

class ImageHttpClientFactory {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                header("x-api-key", BuildConfig.API_KEY)
            }
        }
    }
}