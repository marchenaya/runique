package com.marchenaya.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartClick : RunOverviewAction
    data object OnLogoutCLick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
}