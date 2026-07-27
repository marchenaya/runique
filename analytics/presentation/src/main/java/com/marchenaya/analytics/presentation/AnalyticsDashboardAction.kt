package com.marchenaya.analytics.presentation

sealed interface AnalyticsDashboardAction {
    data object OnBackClick : AnalyticsDashboardAction
}