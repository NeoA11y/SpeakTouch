package com.neo.screenreader

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    override fun onCreate() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d("event: %s, source: %s", event.event, event.source?.className)

        val nodeInfoCompat = event.source?.compat

        when (event.eventType) {

            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {
                nodeInfoCompat?.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
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
}

private val AccessibilityNodeInfo.compat: AccessibilityNodeInfoCompat
    get() = AccessibilityNodeInfoCompat.wrap(this)

private val AccessibilityEvent.event: String
    get() {
        return when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "TYPE_VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> "TYPE_VIEW_LONG_CLICKED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> "TYPE_VIEW_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "TYPE_VIEW_TEXT_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> "TYPE_VIEW_HOVER_ENTER"
            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> "TYPE_VIEW_HOVER_EXIT"
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> "TYPE_NOTIFICATION_STATE_CHANGED"
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START -> {
                "TYPE_TOUCH_EXPLORATION_GESTURE_START"
            }
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> "TYPE_TOUCH_EXPLORATION_GESTURE_END"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "TYPE_WINDOW_CONTENT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> "TYPE_VIEW_TEXT_SELECTION_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "TYPE_VIEW_SCROLLED"
            AccessibilityEvent.TYPE_ANNOUNCEMENT -> "TYPE_ANNOUNCEMENT"
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> "TYPE_VIEW_ACCESSIBILITY_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> {
                "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED"
            }
            AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> {
                "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY"
            }
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> "TYPE_GESTURE_DETECTION_START"
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> "TYPE_GESTURE_DETECTION_END"
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> "TYPE_TOUCH_INTERACTION_START"
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> "TYPE_TOUCH_INTERACTION_END"
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> "TYPE_WINDOWS_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED -> "TYPE_VIEW_CONTEXT_CLICKED"
            AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT -> "TYPE_ASSIST_READING_CONTEXT"
            AccessibilityEvent.TYPE_SPEECH_STATE_CHANGE -> "TYPE_SPEECH_STATE_CHANGE"
            else -> Integer.toHexString(eventType)
        }
    }
