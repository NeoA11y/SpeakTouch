package com.neo.screenreader

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.screenreader.utils.extensions.eventString
import com.neo.screenreader.utils.extensions.isAccessibilityFocusable
import com.neo.screenreader.utils.extensions.isClickableOrFocusable
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    override fun onCreate() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(
            "event: %s, source: %s",
            event.eventString,
            event.source?.className
        )

        when (event.eventType) {

            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {

                val nodeInfo = event.source ?: return

                setAccessibilityFocus(nodeInfo)
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

                val nodeInfoCompat = event.source ?: return

                setAccessibilityFocus(nodeInfoCompat)
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

    private fun setAccessibilityFocus(nodeInfo: AccessibilityNodeInfo) {

        val nodeInfoCompat = AccessibilityNodeInfoCompat.wrap(nodeInfo)

        Timber.d(
            "setAccessibilityFocus()\n" +
                    "class: %s\n" +
                    "isFocusable: %s\n" +
                    "isClickable: %s\n" +
                    "isLongClickable: %s\n" +
                    "text: %s\n" +
                    "description: %s\n" +
                    "isHeading: %s\n" +
                    "isScreenReaderFocusable: %s\n" +
                    "isImportantForAccessibility: %s\n",
            nodeInfoCompat.className,
            nodeInfoCompat.isFocusable,
            nodeInfoCompat.isClickable,
            nodeInfoCompat.isLongClickable,
            nodeInfoCompat.text,
            nodeInfoCompat.contentDescription,
            nodeInfoCompat.isHeading,
            nodeInfoCompat.isScreenReaderFocusable,
            nodeInfoCompat.isImportantForAccessibility,
        )

        val nodeInfoParent = nodeInfoCompat.parent

        when {
            nodeInfoCompat.isClickableOrFocusable -> {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
            }

            nodeInfoCompat.parent?.isClickableOrFocusable == true -> {
                nodeInfoParent.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
            }

            nodeInfo.isAccessibilityFocusable -> {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
            }
        }
    }

    override fun onInterrupt() {

    }
}
