package com.marchenaya.core.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.marchenaya.core.domain.AuthInfo

private const val AUTH_KEYSET = "auth_keyset"
private const val AUTH_KEYSET_PREFS = "auth_keyset_prefs"
private const val AUTH_MASTER_KEY_URI = "android-keystore://auth_master_key"
private const val AUTH_INFO_FILE_NAME = "auth_info.json"

class AuthInfoDataStore(
    private val context: Context
) {

    fun create(): DataStore<AuthInfo?> {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, AUTH_KEYSET, AUTH_KEYSET_PREFS)
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri(AUTH_MASTER_KEY_URI)
            .build()
            .keysetHandle

        val aeadSerializer = AeadSerializer(
            aead =
                keysetHandle.getPrimitive(
                    RegistryConfiguration.get(),
                    Aead::class.java,
                ),
            wrappedSerializer = AuthInfoSerializer,
            associatedData = AUTH_INFO_FILE_NAME.encodeToByteArray(),
        )

        return DataStoreFactory.create(
            serializer = aeadSerializer,
            produceFile = {
                context.dataStoreFile(AUTH_INFO_FILE_NAME)
            }
        )
    }

}

