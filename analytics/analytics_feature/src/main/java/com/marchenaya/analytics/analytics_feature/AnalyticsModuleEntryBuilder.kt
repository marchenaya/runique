package com.marchenaya.analytics.analytics_feature

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.google.android.play.core.splitcompat.SplitCompat
import com.marchenaya.analytics.data.di.analyticsDataModule
import com.marchenaya.analytics.presentation.AnalyticsDashboardScreenRoot
import com.marchenaya.analytics.presentation.di.analyticsPresentationModule
import com.marchenaya.runique.navigation.dynamicfeature.AnalyticsModule
import com.marchenaya.runique.navigation.dynamicfeature.DynamicModuleEntryBuilder
import org.koin.core.context.loadKoinModules

@Suppress("unused")
class AnalyticsModuleEntryBuilder : DynamicModuleEntryBuilder {

    override fun EntryProviderScope<NavKey>.build(onBack: () -> Unit) {
        entry<AnalyticsModule.Dashboard> {
            val context = LocalContext.current
            if (remember { initializeAnalytics(context) }) {
                AnalyticsDashboardScreenRoot(
                    onBackClick = onBack
                )
            }
        }
    }

    private fun initializeAnalytics(context: Context): Boolean {
        SplitCompat.install(context)
        loadKoinModules(listOf(analyticsDataModule, analyticsPresentationModule))
        return true
    }
}
