package com.marchenaya.core.data.networking.httpclient.factory

import com.marchenaya.core.data.BuildConfig
import com.marchenaya.core.data.networking.AccessTokenRequest
import com.marchenaya.core.data.networking.AccessTokenResponse
import com.marchenaya.core.data.networking.Endpoints
import com.marchenaya.core.data.networking.httpclient.applyDefaultConfiguration
import com.marchenaya.core.data.networking.httpclient.post
import com.marchenaya.core.domain.AuthInfo
import com.marchenaya.core.domain.SessionStorage
import com.marchenaya.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.coroutines.flow.firstOrNull

class AuthenticatedHttpClientFactory(
    private val sessionStorage: SessionStorage
) {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            applyDefaultConfiguration(BuildConfig.API_KEY)
            install(Auth) {
                bearer {
                    loadTokens {
                        val info = sessionStorage.observeAuthInfo().firstOrNull()
                        BearerTokens(
                            accessToken = info?.accessToken ?: "",
                            refreshToken = info?.refreshToken ?: ""
                        )
                    }
                    refreshTokens {
                        val info = sessionStorage.observeAuthInfo().firstOrNull()
                        val response = client.post<AccessTokenRequest, AccessTokenResponse>(
                            route = Endpoints.ACCESS_TOKEN,
                            body = AccessTokenRequest(
                                refreshToken = info?.refreshToken ?: "",
                                userId = info?.userId ?: ""
                            )
                        )
                        if (response is Result.Success) {
                            val newAuthInfo = AuthInfo(
                                accessToken = response.data.accessToken,
                                refreshToken = info?.refreshToken ?: "",
                                userId = info?.userId ?: ""
                            )
                            sessionStorage.set(newAuthInfo)

                            BearerTokens(
                                accessToken = newAuthInfo.accessToken,
                                refreshToken = newAuthInfo.refreshToken
                            )
                        } else {
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                }
            }
        }
    }

}