package com.marchenaya.run.data.di

import com.marchenaya.core.domain.run.SyncRunScheduler
import com.marchenaya.run.data.CreateRunWorker
import com.marchenaya.run.data.DeleteRunWorker
import com.marchenaya.run.data.FetchRunsWorker
import com.marchenaya.run.data.SyncRunWorkerScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}