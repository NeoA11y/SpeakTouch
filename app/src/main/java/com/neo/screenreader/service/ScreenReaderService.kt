package com.neo.screenreader.service

import android.accessibilityservice.AccessibilityService
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.screenreader.manager.FocusManager
import com.neo.screenreader.utils.extensions.availableContent
import com.neo.screenreader.utils.extensions.eventString
import com.neo.screenreader.utils.extensions.isEmptyOrNull
import com.neo.screenreader.utils.extensions.isReadable
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    private lateinit var textToSpeech: TextToSpeech

    private val focusManager = FocusManager()

    override fun onCreate() {
        super.onCreate()

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.speak(
                    "Screen Reader ativado",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(
            "event: %s, source: %s",
            event.eventString,
            event.source?.className
        )

        focusManager.handlerAccessibilityEvent(event)

        when (event.eventType) {

            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {

            }

            AccessibilityEvent.TYPE_ANNOUNCEMENT -> {

            }
            AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT -> {

            }
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> {

            }
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> {

            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {

            }
            AccessibilityEvent.TYPE_SPEECH_STATE_CHANGE -> {

            }
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> {

            }
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START -> {

            }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> {

            }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> {

            }
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> {

            }
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> {

                val nodeInfo = event.source ?: return

                textToSpeech.speak(
                    nodeInfo.availableContent.isEmptyOrNull {
                        buildList {
                            for (index in 0 until nodeInfo.childCount) {
                                val child = nodeInfo.getChild(index) ?: continue

                                val childCompat = AccessibilityNodeInfoCompat.wrap(child)

                                if (childCompat.isReadable) {
                                    add(child.availableContent)
                                }
                            }
                        }.joinToString(", ")
                    },
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {

            }
            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED -> {

            }
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {

            }
            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> {

            }
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {

            }
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }
            AccessibilityEvent.TYPE_VIEW_SELECTED -> {

            }
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {

            }
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> {

            }
            AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> {

            }
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> {

            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {

            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {

            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()

        textToSpeech.shutdown();
    }
}