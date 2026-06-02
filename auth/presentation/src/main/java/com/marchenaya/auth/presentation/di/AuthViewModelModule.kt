package com.marchenaya.auth.presentation.di

import com.marchenaya.auth.presentation.login.LoginViewModel
import com.marchenaya.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}