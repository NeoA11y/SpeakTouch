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
import com.neo.speaktouch.utils.extensions.NodeInfo
import com.neo.speaktouch.utils.extensions.getNearestAncestor
import com.neo.speaktouch.utils.extensions.isReadable
import com.neo.speaktouch.utils.extensions.isRequiredFocus

class FocusInterceptor : Interceptor {

    override fun handle(event: AccessibilityEvent) {

        val node = NodeInfo.wrap(event.source ?: return)

        if (node.isAccessibilityFocused) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {
                handlerAccessibilityNode(node)
            }

            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                handlerAccessibilityNode(node)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                handlerAccessibilityNode(node)
            }

            else -> Unit
        }
    }

    private fun handlerAccessibilityNode(node: NodeInfo) {
        getFocusableNode(node)?.run {
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
