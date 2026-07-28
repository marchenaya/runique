package com.marchenaya.runique

sealed interface MainAction {
    data object OnAnalyticsFeatureLoading : MainAction
    data object OnAnalyticsFeatureInstalled : MainAction
    data object OnAnalyticsFeatureInstallFailed : MainAction
    data object OnAnalyticsFeatureLoadFailed : MainAction
}
