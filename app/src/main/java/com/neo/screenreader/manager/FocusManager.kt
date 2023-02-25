package com.neo.screenreader.manager

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.screenreader.utils.extensions.getLog
import com.neo.screenreader.utils.extensions.isActionable
import com.neo.screenreader.utils.extensions.isReadable
import timber.log.Timber

class FocusManager {

    private var lastNode: AccessibilityNodeInfoCompat? = null

    fun handlerAccessibilityEvent(event: AccessibilityEvent) {

        val node = AccessibilityNodeInfoCompat.wrap(event.source ?: return)

        // node information log
        Timber.d(node.getLog())

        if (!node.isImportantForAccessibility) {
            Timber.i("ignored: IMPORTANT_ACCESSIBILITY_NO")
            return
        }

        if (node.isAccessibilityFocused) {
            Timber.i("ignored: ALREADY_ACCESSIBILITY_FOCUSED")
            return
        }

        if (this.lastNode == node) {
            Timber.i("ignored: IS_SAME_NODE")
            return
        } else {
            this.lastNode = node
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {
                Timber.i("event: TYPE_VIEW_HOVER_ENTER")
                handlerAccessibilityNode(node)
            }

            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                Timber.i("event: TYPE_VIEW_FOCUSED")
                handlerAccessibilityNode(node)
            }

            else -> {
                Timber.i("ignored")
            }
        }
    }

    private fun handlerAccessibilityNode(node: AccessibilityNodeInfoCompat) {

        when {

            !node.isImportantForAccessibility -> {
                Timber.i("ignored: IMPORTANT_ACCESSIBILITY_NO")
                return
            }

            node.isActionable -> {
                Timber.i("selected: ACTIONABLE")
                node.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
                return
            }

            node.parent?.isActionable == true -> {
                Timber.i("up parent: ACTIONABLE")
                handlerAccessibilityNode(node.parent)
                return
            }

            node.isReadable -> {
                Timber.i("selected: READABLE")
                node.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
                return
            }

            node.parent?.isReadable == true -> {
                Timber.i("up parent: READABLE")
                handlerAccessibilityNode(node.parent)
            }

            else -> {
                Timber.i("ignored: NOT_ACCESSIBLE")
            }
        }
    }
}
