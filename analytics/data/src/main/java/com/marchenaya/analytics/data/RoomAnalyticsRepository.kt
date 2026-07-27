package com.marchenaya.analytics.data

import com.marchenaya.analytics.domain.AnalyticsRepository
import com.marchenaya.analytics.domain.AnalyticsValues
import com.marchenaya.core.database.dao.AnalyticsDao
import com.marchenaya.core.domain.util.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao,
    private val dispatcherProvider: DispatcherProvider
) : AnalyticsRepository {

    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(dispatcherProvider.io) {
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTimeMillis = async { analyticsDao.getTotalTimeRun() }
            val maxRunSpeed = async { analyticsDao.getMaxRunSpeed() }
            val averageDistancePerRun = async { analyticsDao.getAverageDistancePerRun() }
            val averagePacePerRun = async { analyticsDao.getAveragePacePerRun() }

            AnalyticsValues(
                totalDistanceRun = totalDistance.await(),
                totalTimeRun = totalTimeMillis.await().milliseconds,
                fastestEverRun = maxRunSpeed.await(),
                averageDistancePerRun = averageDistancePerRun.await(),
                averagePacePerRun = averagePacePerRun.await()
            )
        }
    }

}