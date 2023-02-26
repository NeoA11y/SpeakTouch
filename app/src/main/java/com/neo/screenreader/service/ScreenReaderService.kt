package com.neo.screenreader.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.neo.screenreader.intercepter.FocusInterceptor
import com.neo.screenreader.intercepter.SpeechInterceptor
import com.neo.screenreader.intercepter.interfece.Interceptor
import com.neo.screenreader.utils.extensions.getEventLog
import com.neo.screenreader.utils.extensions.getInstance
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    private val interceptors = mutableListOf<Interceptor>()

    override fun onCreate() {
        super.onCreate()

        interceptors.add(FocusInterceptor())
        interceptors.add(SpeechInterceptor.getInstance(this))
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(
            "EVENT: %s, SOURCE: %s",
            event.getEventLog(),
            event.source?.className?.ifEmpty { "unknown" }
        )

        interceptors.forEach { it.handler(event) }
    }

    override fun onDestroy() {
        super.onDestroy()

        interceptors
            .getInstance<SpeechInterceptor>()
            .shutdown()

        interceptors.clear()
    }

    override fun onInterrupt() = Unit
}