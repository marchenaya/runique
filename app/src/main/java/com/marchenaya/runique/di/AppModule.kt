package com.marchenaya.runique.di

import com.marchenaya.runique.MainViewModel
import com.marchenaya.runique.RuniqueApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as RuniqueApp).applicationScope
    }
    viewModelOf(::MainViewModel)
}