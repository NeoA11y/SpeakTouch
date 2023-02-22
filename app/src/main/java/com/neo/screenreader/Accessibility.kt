package com.neo.screenreader

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class Accessibility : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("Accessibility", event.toString())
    }

    override fun onInterrupt() {

    }
}