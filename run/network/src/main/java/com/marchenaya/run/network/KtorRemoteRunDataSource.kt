package com.marchenaya.run.network

import com.marchenaya.core.data.networking.Endpoints.DELETE_RUN
import com.marchenaya.core.data.networking.Endpoints.GET_RUNS
import com.marchenaya.core.data.networking.Endpoints.POST_RUN
import com.marchenaya.core.data.networking.httpclient.constructRoute
import com.marchenaya.core.data.networking.httpclient.delete
import com.marchenaya.core.data.networking.httpclient.get
import com.marchenaya.core.data.networking.httpclient.safeCall
import com.marchenaya.core.domain.run.RemoteRunDataSource
import com.marchenaya.core.domain.run.Run
import com.marchenaya.core.domain.util.DataError
import com.marchenaya.core.domain.util.EmptyResult
import com.marchenaya.core.domain.util.Result
import com.marchenaya.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json

class KtorRemoteRunDataSource(
    private val httpClient: HttpClient
) : RemoteRunDataSource {

    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        return httpClient.get<List<RunDto>>(
            route = GET_RUNS
        ).map { runDtos ->
            runDtos.map {
                it.toRun()
            }
        }
    }

    override suspend fun postRun(
        run: Run,
        mapPicture: ByteArray
    ): Result<Run, DataError.Network> {
        val createRunRequestJson = Json.encodeToString(run.toCreateRunRequest())
        val result = safeCall<RunDto> {
            httpClient.submitFormWithBinaryData(
                url = constructRoute(POST_RUN),
                formData = formData {
                    append(KEY_MAP_PICTURE, mapPicture, Headers.build {
                        append(HttpHeaders.ContentType, CONTENT_TYPE_IMAGE)
                        append(HttpHeaders.ContentDisposition, MAP_PICTURE_DISPOSITION)
                    })
                    append(KEY_RUN_DATA, createRunRequestJson, Headers.build {
                        append(HttpHeaders.ContentType, CONTENT_TYPE_TEXT)
                        append(HttpHeaders.ContentDisposition, RUN_DATA_DISPOSITION)
                    })
                }
            ) {
                method = HttpMethod.Post
            }
        }
        return result.map {
            it.toRun()
        }
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete<Unit>(
            route = DELETE_RUN,
            queryParameters = mapOf(
                QUERY_PARAM_ID to id
            )
        )
    }

    companion object {
        private const val KEY_MAP_PICTURE = "MAP_PICTURE"
        private const val KEY_RUN_DATA = "RUN_DATA"
        private const val CONTENT_TYPE_IMAGE = "image/jpeg"
        private const val CONTENT_TYPE_TEXT = "text/plain"
        private const val MAP_PICTURE_DISPOSITION = "filename=mappicture.jpg"
        private const val RUN_DATA_DISPOSITION = "form-data; name=\"RUN_DATA\""
        private const val QUERY_PARAM_ID = "id"
    }

}