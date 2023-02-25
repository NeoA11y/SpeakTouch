package com.neo.screenreader.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.neo.screenreader.manager.FocusManager
import com.neo.screenreader.manager.SpeechManager
import com.neo.screenreader.utils.extensions.getEventLog
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    private lateinit var speechManager: SpeechManager

    private val focusManager = FocusManager()

    override fun onCreate() {
        super.onCreate()

        speechManager = SpeechManager.getInstance(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(
            "EVENT: %s\n" +
                    "SOURCE: %s",
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