package com.neo.screenreader.intercepter

import android.view.accessibility.AccessibilityEvent
import com.neo.screenreader.intercepter.interfece.Interceptor
import com.neo.screenreader.utils.extensions.NodeInfo
import com.neo.screenreader.utils.extensions.getLog
import com.neo.screenreader.utils.extensions.isActionable
import com.neo.screenreader.utils.extensions.isReadable
import timber.log.Timber

class FocusInterceptor : Interceptor {

    private var lastNode: NodeInfo? = null

    override fun handler(event: AccessibilityEvent) {

        val node = NodeInfo.wrap(event.source ?: return)

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

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                Timber.i("event: TYPE_VIEW_CLICKED")
                handlerAccessibilityNode(node)
            }

            else -> {
                Timber.i("ignored")
            }
        }
    }

    private fun handlerAccessibilityNode(node: NodeInfo) {

        when {

            !node.isImportantForAccessibility -> {
                Timber.i("ignored: IMPORTANT_ACCESSIBILITY_NO")
                return
            }

            node.isActionable -> {
                Timber.i("selected: ACTIONABLE")
                node.performAction(NodeInfo.ACTION_ACCESSIBILITY_FOCUS)
                return
            }

            node.parent?.isActionable == true -> {
                Timber.i("up parent: ACTIONABLE")
                handlerAccessibilityNode(node.parent)
                return
            }

            node.isReadable -> {
                Timber.i("selected: READABLE")
                node.performAction(NodeInfo.ACTION_ACCESSIBILITY_FOCUS)
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
