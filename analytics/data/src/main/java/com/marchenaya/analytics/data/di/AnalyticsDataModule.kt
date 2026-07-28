package com.marchenaya.analytics.data.di

import com.marchenaya.analytics.data.RoomAnalyticsRepository
import com.marchenaya.analytics.domain.AnalyticsRepository
import com.marchenaya.core.database.RunDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsDataModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single {
        get<RunDatabase>().analyticsDao
    }
}