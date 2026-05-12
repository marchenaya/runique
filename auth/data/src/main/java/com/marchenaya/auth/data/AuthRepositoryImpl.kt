package com.marchenaya.auth.data

import com.marchenaya.auth.domain.AuthRepository
import com.marchenaya.core.data.networking.post
import com.marchenaya.core.domain.util.DataError
import com.marchenaya.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }

}