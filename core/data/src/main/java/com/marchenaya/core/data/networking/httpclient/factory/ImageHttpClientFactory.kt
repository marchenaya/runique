package com.marchenaya.core.data.networking.httpclient.factory

import com.marchenaya.core.data.BuildConfig
import com.marchenaya.core.data.networking.httpclient.applyDefaultConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class ImageHttpClientFactory {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            applyDefaultConfiguration(BuildConfig.API_KEY, serializeJson = false)
        }
    }
}