package com.marchenaya.auth.data.di

import com.marchenaya.auth.data.AuthRepositoryImpl
import com.marchenaya.auth.data.EmailPatternValidator
import com.marchenaya.auth.data.networking.UnauthenticatedHttpClientFactory
import com.marchenaya.auth.domain.AuthRepository
import com.marchenaya.auth.domain.PatternValidator
import com.marchenaya.auth.domain.UserDataValidator
import com.marchenaya.core.data.networking.httpclient.HttpClientType
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::UserDataValidator)
    singleOf(::EmailPatternValidator).bind<PatternValidator>()
    single<AuthRepository> {
        AuthRepositoryImpl(get(named(HttpClientType.Unauthenticated)), get())
    }
    single(named(HttpClientType.Unauthenticated)) {
        UnauthenticatedHttpClientFactory().build()
    }
}