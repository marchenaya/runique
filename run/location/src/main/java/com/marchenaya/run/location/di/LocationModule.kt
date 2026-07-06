package com.marchenaya.run.location.di

import com.marchenaya.run.domain.LocationObserver
import com.marchenaya.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}