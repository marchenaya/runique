package com.marchenaya.core.data.di

import com.marchenaya.core.data.auth.AuthInfoDataStore
import com.marchenaya.core.data.auth.DataStoreSessionStorage
import com.marchenaya.core.data.networking.HttpClientFactory
import com.marchenaya.core.domain.SessionStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
    singleOf(::DataStoreSessionStorage).bind<SessionStorage>()
    single { AuthInfoDataStore(androidContext()) }
    single { get<AuthInfoDataStore>().create() }
}