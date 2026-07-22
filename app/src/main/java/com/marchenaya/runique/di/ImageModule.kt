package com.marchenaya.runique.di

import coil3.ImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.marchenaya.core.data.networking.HttpClientType
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val imageModule = module {
    single {
        ImageLoader.Builder(androidContext())
            .components {
                add(KtorNetworkFetcherFactory(httpClient = get<HttpClient>(named(HttpClientType.Image))))
            }
            .build()
    }
}