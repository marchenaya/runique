package com.marchenaya.core.data.di

import com.marchenaya.core.data.auth.EncryptedSessionStorage
import com.marchenaya.core.data.networking.HttpClientFactory
import com.marchenaya.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}