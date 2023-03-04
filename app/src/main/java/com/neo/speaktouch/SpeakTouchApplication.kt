package com.neo.speaktouch

import android.app.Application
import timber.log.Timber

class SpeakTouchApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}