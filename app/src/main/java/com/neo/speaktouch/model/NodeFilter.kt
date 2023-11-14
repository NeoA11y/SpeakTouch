/*
 * Node filters.
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

package com.neo.speaktouch.model

import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.utils.extension.getNearestAncestor
import com.neo.speaktouch.utils.`object`.NodeValidator

sealed interface NodeFilter {
    fun filter(node: AccessibilityNodeInfo): Boolean

    object Focusable : NodeFilter {
        override fun filter(node: AccessibilityNodeInfo): Boolean {
            val compat = AccessibilityNodeInfoCompat.wrap(node)

            if (!NodeValidator.isValidForAccessibility(compat)) return false

            if (NodeValidator.mustFocus(compat)) return true

            return NodeValidator.hasContentToRead(compat) && !mustFocusOnAncestor(compat)
        }

        private fun mustFocusOnAncestor(
            node: AccessibilityNodeInfoCompat
        ): Boolean {
            return node.getNearestAncestor {
                NodeValidator.mustFocus(it)
            } != null
        }
    }
}
