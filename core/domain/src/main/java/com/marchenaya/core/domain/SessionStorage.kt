package com.marchenaya.core.domain

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun set(newInfo: AuthInfo?)
}