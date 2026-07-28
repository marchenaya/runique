package com.marchenaya.runique.navigation.dynamicfeature

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object AnalyticsModule : DynamicModule(
    entryBuilderClassName = "com.marchenaya.analytics.analytics_feature.AnalyticsEntryBuilder",
    moduleName = "analytics_feature"
) {
    @Serializable
    data object Dashboard : NavKey
}