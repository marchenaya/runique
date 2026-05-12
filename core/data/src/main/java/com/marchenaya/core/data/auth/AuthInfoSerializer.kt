package com.marchenaya.core.data.auth

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.marchenaya.core.domain.AuthInfo
import com.marchenaya.core.domain.util.DispatcherProvider
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class AuthInfoSerializer(
    private val dispatcherProvider: DispatcherProvider
) : Serializer<AuthInfo?> {
    override val defaultValue: AuthInfo? = null

    override suspend fun readFrom(input: InputStream): AuthInfo? {
        return try {
            Json.decodeFromString<AuthInfoSerializable>(
                input.readBytes().decodeToString()
            ).toAuthInfo()
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read AuthInfo", serialization)
        }
    }

    override suspend fun writeTo(t: AuthInfo?, output: OutputStream) {
        withContext(dispatcherProvider.io) {
            val json = Json.encodeToString(t?.toAuthInfoSerializable())
            output.write(json.encodeToByteArray())
        }
    }
}
