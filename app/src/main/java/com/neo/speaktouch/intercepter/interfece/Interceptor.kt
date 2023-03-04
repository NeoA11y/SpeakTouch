package com.neo.speaktouch.intercepter.interfece

import android.view.accessibility.AccessibilityEvent

interface Interceptor {
    fun handler(event: AccessibilityEvent)
}