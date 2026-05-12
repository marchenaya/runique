package com.marchenaya.core.data.auth

import androidx.datastore.core.DataStore
import com.marchenaya.core.domain.AuthInfo
import com.marchenaya.core.domain.SessionStorage
import kotlinx.coroutines.flow.Flow

class DataStoreSessionStorage(
    private val dataStore: DataStore<AuthInfo?>
) : SessionStorage {

    override fun observeAuthInfo(): Flow<AuthInfo?> = dataStore.data

    override suspend fun set(newInfo: AuthInfo?) {
        dataStore.updateData { _ ->
            newInfo
        }
    }

}
