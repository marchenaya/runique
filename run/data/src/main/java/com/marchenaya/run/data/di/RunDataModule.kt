package com.marchenaya.run.data.di

import com.marchenaya.run.data.CreateRunWorker
import com.marchenaya.run.data.DeleteRunWorker
import com.marchenaya.run.data.FetchRunWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunWorker)
    workerOf(::DeleteRunWorker)
}