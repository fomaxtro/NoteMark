package com.fomaxtro.notemark

import android.app.Application
import com.fomaxtro.notemark.data.di.dataModule
import com.fomaxtro.notemark.di.mainModule
import com.fomaxtro.notemark.domain.di.domainModule
import com.fomaxtro.notemark.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@NoteMarkApplication)
            workManagerFactory()
            modules(
                domainModule,
                presentationModule,
                dataModule,
                mainModule
            )
        }
    }
}