package com.neo.speaktouch.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.FocusInterceptor
import com.neo.speaktouch.intercepter.SpeechInterceptor
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.utils.extensions.getLog
import com.neo.speaktouch.utils.extensions.getInstance
import timber.log.Timber

class SpeakTouchService : AccessibilityService() {

    private val interceptors = mutableListOf<Interceptor>()

    override fun onCreate() {
        super.onCreate()

        interceptors.add(FocusInterceptor())
        interceptors.add(SpeechInterceptor.getInstance(this))
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(event.getLog())

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