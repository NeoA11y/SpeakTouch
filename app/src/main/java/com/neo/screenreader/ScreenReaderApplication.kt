package com.neo.screenreader

import android.app.Application
import timber.log.Timber

class ScreenReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}