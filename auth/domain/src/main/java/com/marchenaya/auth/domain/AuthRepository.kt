package com.marchenaya.auth.domain

import com.marchenaya.core.domain.util.DataError
import com.marchenaya.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>

    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>

}