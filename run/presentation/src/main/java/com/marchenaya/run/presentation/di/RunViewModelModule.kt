package com.marchenaya.run.presentation.di

import com.marchenaya.run.presentation.active_run.ActiveRunViewModel
import com.marchenaya.run.presentation.run_overview.RunOverviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}