package com.marchenaya.core.data.run

import com.marchenaya.core.data.networking.Endpoints.LOGOUT
import com.marchenaya.core.data.networking.httpclient.get
import com.marchenaya.core.database.dao.RunPendingSyncDao
import com.marchenaya.core.database.mapper.toRun
import com.marchenaya.core.domain.SessionStorage
import com.marchenaya.core.domain.run.LocalRunDataSource
import com.marchenaya.core.domain.run.RemoteRunDataSource
import com.marchenaya.core.domain.run.Run
import com.marchenaya.core.domain.run.RunId
import com.marchenaya.core.domain.run.RunRepository
import com.marchenaya.core.domain.run.SyncRunScheduler
import com.marchenaya.core.domain.util.DataError
import com.marchenaya.core.domain.util.DispatcherProvider
import com.marchenaya.core.domain.util.EmptyResult
import com.marchenaya.core.domain.util.Result
import com.marchenaya.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val dispatcherProvider: DispatcherProvider,
    private val syncRunScheduler: SyncRunScheduler,
    private val client: HttpClient
) : RunRepository {

    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    //todo : And obviously, you could also actually save the byte array together with each local run and then set that to null once you get the image URL. That's actually a cool optimization you could stick to here, but I'll leave that as a little homework.
    //I'm personally just not too big of a fan to save too many byte arrays in database fields, but if it's really just for those runs you couldn't yet sync, I think it would be beneficial to also save the images as a byte array so you can display them locally and then replace that byte array with a null once you sync them with the remote API and get the URL
    override suspend fun upsertRun(
        run: Run,
        mapPicture: ByteArray
    ): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val runWithId = run.copy(id = localResult.data)
        val remoteResult = remoteRunDataSource.postRun(
            run = runWithId,
            mapPicture = mapPicture
        )

        return when (remoteResult) {
            is Result.Error -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        type = SyncRunScheduler.SyncType.CreateRun(
                            run = runWithId,
                            mapPicturesBytes = mapPicture
                        )
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        // Edge case where the run is created in offline-mode,
        // and then deleted in offline-mode as well. In that case,
        // we don't need to sync anything.
        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                    type = SyncRunScheduler.SyncType.DeleteRun(id)
                )
            }.join()
        }
    }

    override suspend fun syncPendingRuns() {
        withContext(dispatcherProvider.io) {
            val userId = sessionStorage.observeAuthInfo().firstOrNull()?.userId
                ?: return@withContext

            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntities(userId)
            }

            val deletedRuns = async {
                runPendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createJobs = createdRuns
                .await()
                .map {
                    launch {
                        val run = it.run.toRun()
                        when (remoteRunDataSource.postRun(run, it.mapPictureBytes)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            val deleteJobs = deletedRuns
                .await()
                .map {
                    launch {
                        when (remoteRunDataSource.deleteRun(it.runId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            createJobs.joinAll()
            deleteJobs.joinAll()

        }
    }

    override suspend fun deleteAllRuns() {
        localRunDataSource.deleteAllRuns()
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        val result = client.get<Unit>(
            route = LOGOUT
        ).asEmptyDataResult()

        client.authProvider<BearerAuthProvider>()?.clearToken()

        return result
    }

}