package com.neo.screenreader

import android.accessibilityservice.AccessibilityService
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.screenreader.utils.extensions.*
import timber.log.Timber

class ScreenReaderService : AccessibilityService() {

    private lateinit var textToSpeech: TextToSpeech

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

        when (event.eventType) {

            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {

                val nodeInfo = event.source ?: return

                handlerAccessibilityFocus(nodeInfo)
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

                val nodeInfoCompat = event.source ?: return

                handlerAccessibilityFocus(nodeInfoCompat)
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

    private fun handlerAccessibilityFocus(nodeInfo: AccessibilityNodeInfo) {

        val nodeInfoCompat = AccessibilityNodeInfoCompat.wrap(nodeInfo)

        Timber.d(
            "setAccessibilityFocus()\n" +
                    "class: %s\n" +
                    "isFocusable: %s\n" +
                    "isEnabled: %s\n" +
                    "isClickable: %s\n" +
                    "isLongClickable: %s\n" +
                    "text: %s\n" +
                    "hint: %s\n" +
                    "contentDescription: %s\n" +
                    "stateDescription: %s\n" +
                    "error: %s\n" +
                    "isHeading: %s\n" +
                    "paneTitle: %s\n" +
                    "isScreenReaderFocusable: %s\n" +
                    "isImportantForAccessibility: %s\n" +
                    "parent: %s\n",
            nodeInfoCompat.className,
            nodeInfoCompat.isEnabled,
            nodeInfoCompat.isFocusable,
            nodeInfoCompat.isClickable,
            nodeInfoCompat.isLongClickable,
            nodeInfoCompat.text,
            nodeInfoCompat.hintText,
            nodeInfoCompat.contentDescription,
            nodeInfoCompat.stateDescription,
            nodeInfoCompat.error,
            nodeInfoCompat.isHeading,
            nodeInfoCompat.paneTitle,
            nodeInfoCompat.isScreenReaderFocusable,
            nodeInfoCompat.isImportantForAccessibility,
            nodeInfoCompat.parent?.className,
        )

        when {

            !nodeInfoCompat.isImportantForAccessibility -> {
                Timber.i("%s: ignored by important accessibility no", nodeInfoCompat.className)
                return
            }

            nodeInfoCompat.isActionable -> {
                Timber.i("%s: selected by actionable", nodeInfoCompat.className)
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
                return
            }

            nodeInfoCompat.parent?.isActionable == true -> {
                Timber.i("%s: up parent by actionable", nodeInfoCompat.className)
                handlerAccessibilityFocus(nodeInfoCompat.parent.unwrap())
                return
            }

            nodeInfoCompat.isReadable -> {
                Timber.i("%s: selected by readable", nodeInfoCompat.className)
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
                return
            }

            nodeInfoCompat.parent?.isReadable == true -> {
                Timber.i("%s: up parent by readable", nodeInfoCompat.className)
                handlerAccessibilityFocus(nodeInfoCompat.parent.unwrap())
            }

            else -> {
                Timber.i("%s: ignored", nodeInfoCompat.className)
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

private val AccessibilityNodeInfo.availableContent: CharSequence
    get() = text.isEmptyOrNull { contentDescription ?: "" }