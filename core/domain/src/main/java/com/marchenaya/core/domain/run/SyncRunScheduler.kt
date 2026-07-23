package com.marchenaya.core.domain.run

import kotlin.time.Duration

interface SyncRunScheduler {

    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType {
        data class FetchRuns(val interval: Duration) : SyncType
        data class DeleteRun(val runId: RunId) : SyncType
        data class CreateRun(val run: Run, val mapPicturesBytes: ByteArray) : SyncType {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as CreateRun

                if (run != other.run) return false
                if (!mapPicturesBytes.contentEquals(other.mapPicturesBytes)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = run.hashCode()
                result = 31 * result + mapPicturesBytes.contentHashCode()
                return result
            }
        }
    }

}