package com.marchenaya.auth.data.di

import com.marchenaya.auth.data.EmailPatternValidator
import com.marchenaya.auth.domain.PatternValidator
import com.marchenaya.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
}