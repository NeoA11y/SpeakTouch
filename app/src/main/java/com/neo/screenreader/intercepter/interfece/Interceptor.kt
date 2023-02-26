package com.neo.screenreader.intercepter.interfece

import android.view.accessibility.AccessibilityEvent

interface Interceptor {
    fun handler(event: AccessibilityEvent)
}