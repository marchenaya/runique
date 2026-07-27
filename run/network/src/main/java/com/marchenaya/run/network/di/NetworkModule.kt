package com.marchenaya.run.network.di

import com.marchenaya.core.data.networking.httpclient.HttpClientType
import com.marchenaya.core.domain.run.RemoteRunDataSource
import com.marchenaya.run.network.KtorRemoteRunDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single {
        KtorRemoteRunDataSource(get(named(HttpClientType.Authenticated)))
    }.bind<RemoteRunDataSource>()
}