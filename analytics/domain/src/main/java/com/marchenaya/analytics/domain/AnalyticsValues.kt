package com.marchenaya.analytics.domain

import kotlin.time.Duration

data class AnalyticsValues(
    val totalDistanceRun: Int = 0,
    val totalTimeRun: Duration = Duration.ZERO,
    val fastestEverRun: Double = .0,
    val averageDistancePerRun: Double = .0,
    val averagePacePerRun: Double = .0
)
