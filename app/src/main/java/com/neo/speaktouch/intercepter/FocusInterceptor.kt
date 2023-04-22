package com.neo.speaktouch.intercepter

import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.utils.extensions.*
import timber.log.Timber

class FocusInterceptor : Interceptor {

    override fun handle(event: AccessibilityEvent) {

        val node = NodeInfo.wrap(event.source ?: return)

        Timber.d(node.getLog())

        if (node.isAccessibilityFocused) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {
                Timber.d("event: TYPE_VIEW_HOVER_ENTER")
                handlerAccessibilityNode(node)
            }

            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                Timber.d("event: TYPE_VIEW_FOCUSED")
                handlerAccessibilityNode(node)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                Timber.d("event: TYPE_VIEW_CLICKED")
                handlerAccessibilityNode(node)
            }

            else -> Unit
        }
    }

    private fun handlerAccessibilityNode(node: NodeInfo) {
        getFocusableNode(node)?.run {
            Timber.i("performAction: $className")
            performAction(NodeInfo.ACTION_ACCESSIBILITY_FOCUS)
        }
    }

    private fun getFocusableNode(node: NodeInfo): NodeInfo? {

        if (node.isRequiredFocus) {
            // Priority 1
            return node
        }

        // Priority 2
        val nearestAncestor = node.getNearestAncestor {
            it.isRequiredFocus
        }

        return nearestAncestor ?: when {
            node.isReadable -> node // Priority 3
            else -> null
        }
    }
}
