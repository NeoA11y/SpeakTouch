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

package com.neo.speaktouch.intercepter.event

import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.intercepter.event.contract.EventInterceptor
import com.neo.speaktouch.utils.NodeValidator
import com.neo.speaktouch.utils.extension.getNearestAncestor
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class FocusInterceptor @Inject constructor() : EventInterceptor {

    override fun handle(event: AccessibilityEvent) {

        val nodeInfo = AccessibilityNodeInfoCompat.wrap(event.source ?: return)

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER,
            AccessibilityEvent.TYPE_VIEW_FOCUSED,
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                handlerAccessibilityNode(nodeInfo)
            }

            else -> Unit
        }
    }

    private fun handlerAccessibilityNode(
        nodeInfo: AccessibilityNodeInfoCompat
    ) {

        val focusableNode = getFocusableNode(nodeInfo) ?: return

        if (focusableNode.isAccessibilityFocused) return

        focusableNode.performAction(AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS)
    }

    private fun getFocusableNode(
        nodeInfo: AccessibilityNodeInfoCompat
    ): AccessibilityNodeInfoCompat? {

        if (NodeValidator.mustFocus(nodeInfo)) {
            return nodeInfo
        }

        val ancestor = nodeInfo.getNearestAncestor(
            NodeValidator::mustFocus
        )

        return ancestor ?: when {
            NodeValidator.hasReadableContent(nodeInfo) -> nodeInfo
            else -> null
        }
    }
}
