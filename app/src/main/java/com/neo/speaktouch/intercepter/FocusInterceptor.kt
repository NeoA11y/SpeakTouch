/*
 * Intercept focus events.
 *
 * Copyright (C) 2023 Irineu A. Silva.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.neo.speaktouch.intercepter

import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.utils.extensions.*
import timber.log.Timber

class FocusInterceptor : Interceptor {

    override fun handler(event: AccessibilityEvent) {

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
            return node
        }

        val nearestAncestor = node.getNearestAncestor {
            it.isRequiredFocus
        }

        return nearestAncestor ?: when {
            node.isReadable -> node
            else -> null
        }
    }
}
