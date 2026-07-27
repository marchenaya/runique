package com.marchenaya.core.data.di

import com.marchenaya.core.data.auth.AuthInfoDataStore
import com.marchenaya.core.data.auth.AuthInfoSerializer
import com.marchenaya.core.data.auth.DataStoreSessionStorage
import com.marchenaya.core.data.networking.httpclient.HttpClientType
import com.marchenaya.core.data.networking.httpclient.factory.AuthenticatedHttpClientFactory
import com.marchenaya.core.data.networking.httpclient.factory.ImageHttpClientFactory
import com.marchenaya.core.data.run.OfflineFirstRunRepository
import com.marchenaya.core.data.util.DefaultDispatcherProvider
import com.marchenaya.core.domain.SessionStorage
import com.marchenaya.core.domain.run.RunRepository
import com.marchenaya.core.domain.util.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single(named(HttpClientType.Authenticated)) {
        AuthenticatedHttpClientFactory(get()).build()
    }

    single(named(HttpClientType.Image)) {
        ImageHttpClientFactory().build()
    }

    single { get<AuthInfoDataStore>().create() }
    singleOf(::DataStoreSessionStorage).bind<SessionStorage>()
    singleOf(::DefaultDispatcherProvider).bind<DispatcherProvider>()
    singleOf(::AuthInfoSerializer)
    singleOf(::AuthInfoDataStore)

    single<RunRepository> {
        OfflineFirstRunRepository(
            localRunDataSource = get(),
            remoteRunDataSource = get(),
            applicationScope = get(),
            runPendingSyncDao = get(),
            sessionStorage = get(),
            dispatcherProvider = get(),
            syncRunScheduler = get(),
            client = get(named(HttpClientType.Authenticated))
        )
    }
}