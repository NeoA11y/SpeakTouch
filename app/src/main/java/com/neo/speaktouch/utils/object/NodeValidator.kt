/*
 * Node focus and read validations.
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

package com.neo.speaktouch.utils.`object`

import com.neo.speaktouch.utils.extension.isNotNullOrEmpty
import com.neo.speaktouch.utils.extension.iterator
import com.neo.speaktouch.utils.`typealias`.NodeInfo

object NodeValidator {

    private fun isClickable(node: NodeInfo): Boolean {
        return node.isClickable || node.isLongClickable
    }

    private fun hasText(node: NodeInfo): Boolean {
        return listOf(
            node.text,
            node.hintText,
            node.contentDescription
        ).any { it.isNotNullOrEmpty() }
    }

    private fun isRequiredRead(node: NodeInfo): Boolean {
        return node.isFocusable && isReadable(node)
    }

    private fun hasReadableChild(nodeInfo: NodeInfo): Boolean {
        for (child in nodeInfo) {
            if (isReadableAsChild(child)) return true
            if (hasReadableChild(child)) return true
        }

        return false
    }

    fun isValidForAccessible(node: NodeInfo): Boolean {
        return node.isImportantForAccessibility && node.isVisibleToUser
    }

    fun isRequiredFocus(node: NodeInfo): Boolean {
        return isRequiredRead(node) ||
                isClickable(node) &&
                hasReadableChild(node)
    }

    fun isReadable(node: NodeInfo): Boolean {
        return hasText(node) || node.isCheckable
    }

    fun isReadableAsChild(nodeInfo: NodeInfo): Boolean {
        return isReadable(nodeInfo) && !isRequiredFocus(nodeInfo)
    }
}