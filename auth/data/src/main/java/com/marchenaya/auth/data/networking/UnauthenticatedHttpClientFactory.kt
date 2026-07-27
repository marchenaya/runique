package com.marchenaya.auth.data.networking

import com.marchenaya.auth.data.BuildConfig
import com.marchenaya.core.data.networking.httpclient.applyDefaultConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class UnauthenticatedHttpClientFactory {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            applyDefaultConfiguration(BuildConfig.API_KEY)
        }
    }

}