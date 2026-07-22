package com.marchenaya.runique

import android.app.Application
import coil3.SingletonImageLoader
import com.marchenaya.auth.data.di.authDataModule
import com.marchenaya.auth.presentation.di.authViewModelModule
import com.marchenaya.core.data.di.coreDataModule
import com.marchenaya.core.database.di.databaseModule
import com.marchenaya.run.location.di.locationModule
import com.marchenaya.run.network.di.networkModule
import com.marchenaya.run.presentation.di.runPresentationModule
import com.marchenaya.runique.di.appModule
import com.marchenaya.runique.di.imageModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                appModule,
                authDataModule,
                authViewModelModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule,
                imageModule
            )
        }

        SingletonImageLoader.setSafe { get() }
    }
}