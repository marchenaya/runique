package com.marchenaya.core.data.auth

import androidx.datastore.core.DataStore
import com.marchenaya.core.domain.AuthInfo
import com.marchenaya.core.domain.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSessionStorage(
    private val dataStore: DataStore<AuthInfo?>
) : SessionStorage {

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return dataStore.data.map { authInfo ->
            authInfo
        }
    }

    override suspend fun set(newInfo: AuthInfo?) {
        if (newInfo == null) {
            dataStore.updateData { null }
            return
        }
        dataStore.updateData { _ ->
            newInfo
        }
    }

}
