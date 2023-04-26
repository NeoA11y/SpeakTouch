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
import com.neo.speaktouch.utils.`object`.NodeValidator
import com.neo.speaktouch.utils.`typealias`.NodeInfo
import com.neo.speaktouch.utils.extension.getNearestAncestor

class FocusInterceptor : Interceptor {

    override fun handle(event: AccessibilityEvent) {

        val nodeInfo = NodeInfo.wrap(event.source ?: return)

        if (nodeInfo.isAccessibilityFocused) return

        if (!NodeValidator.isValidForAccessible(nodeInfo)) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> {
                handlerAccessibilityNode(nodeInfo)
            }

            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                handlerAccessibilityNode(nodeInfo)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                handlerAccessibilityNode(nodeInfo)
            }

            else -> Unit
        }
    }

    private fun handlerAccessibilityNode(nodeInfo: NodeInfo) {
        getFocusableNode(nodeInfo)?.run {
            performAction(NodeInfo.ACTION_ACCESSIBILITY_FOCUS)
        }
    }

    private fun getFocusableNode(nodeInfo: NodeInfo): NodeInfo? {

        if (NodeValidator.isRequiredFocus(nodeInfo)) {
            return nodeInfo
        }

        val ancestor = nodeInfo.getNearestAncestor(
            NodeValidator::isRequiredFocus
        )

        return ancestor ?: when {
            NodeValidator.isReadable(nodeInfo) -> nodeInfo
            else -> null
        }
    }
}
