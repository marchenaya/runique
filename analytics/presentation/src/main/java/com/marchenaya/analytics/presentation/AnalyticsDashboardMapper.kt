package com.marchenaya.analytics.presentation

import com.marchenaya.analytics.domain.AnalyticsValues
import com.marchenaya.core.presentation.ui.UiText
import com.marchenaya.core.presentation.ui.formatted
import com.marchenaya.core.presentation.ui.toFormattedKm
import com.marchenaya.core.presentation.ui.toFormattedKmH
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun Duration.toFormattedTotalTime(): UiText {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60

    return UiText.StringResource(R.string.format_total_time, days, hours, minutes)
}

fun AnalyticsValues.toAnalyticsDashboardState(): AnalyticsDashboardState {
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmH(),
        averageDistance = (averageDistancePerRun / 1000.0).toFormattedKm(),
        averagePace = averagePacePerRun.seconds.formatted()
    )
}