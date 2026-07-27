package com.marchenaya.analytics.presentation

import com.marchenaya.core.presentation.ui.UiText

data class AnalyticsDashboardState(
    val totalDistanceRun: UiText,
    val totalTimeRun: UiText,
    val fastestEverRun: UiText,
    val averageDistance: UiText,
    val averagePace: UiText
)