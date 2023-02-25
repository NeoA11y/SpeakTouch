package com.neo.screenreader.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.neo.screenreader.manager.FocusManager
import com.neo.screenreader.manager.SpeechManager
import com.neo.screenreader.utils.extensions.getEventLog
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    private val speechManager: SpeechManager by lazy {
        SpeechManager.getInstance(this)
    }

    private val focusManager = FocusManager()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(
            "EVENT: %s, SOURCE: %s",
            event.getEventLog(),
            event.source?.className?.ifEmpty { "unknown" }
        )

        focusManager.handlerAccessibilityEvent(event)
        speechManager.handlerAccessibilityEvent(event)
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()

        speechManager.shutdown();
    }
}