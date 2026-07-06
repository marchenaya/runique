package com.marchenaya.run.presentation.di

import com.marchenaya.run.domain.RunningTracker
import com.marchenaya.run.presentation.active_run.ActiveRunViewModel
import com.marchenaya.run.presentation.run_overview.RunOverviewViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}