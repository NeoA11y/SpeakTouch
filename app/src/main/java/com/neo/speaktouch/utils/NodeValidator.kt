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

package com.neo.speaktouch.utils

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.utils.extension.isNotNullOrEmpty
import com.neo.speaktouch.utils.extension.iterator

object NodeValidator {

    /**
     * @return true if [node] has any click
     */
    private fun isClickable(node: AccessibilityNodeInfoCompat): Boolean {

        return node.isClickable || node.isLongClickable
    }

    /**
     * @return true if [node] has any interaction
     */
    private fun hasInteraction(node: AccessibilityNodeInfoCompat): Boolean {

        return isClickable(node) || node.isFocusable
    }

    /**
     * @return true if [node] has some text to read
     */
    private fun hasTextToRead(node: AccessibilityNodeInfoCompat): Boolean {

        return listOf(
            node.text,
            node.hintText?.takeIf { node.isEditable },
            node.contentDescription?.takeIf { !node.isEditable }
        ).any { it.isNotNullOrEmpty() }
    }

    /**
     * @return true if [node] should be read directly
     */
    private fun mustReadContent(node: AccessibilityNodeInfoCompat): Boolean {

        return hasContentToRead(node) && hasInteraction(node)
    }

    /**
     * @return true if [node] has a readable child
     */
    private fun hasReadableChild(node: AccessibilityNodeInfoCompat): Boolean {

        for (child in node) {
            if (!isValidForAccessibility(child)) continue
            if (isReadableAsChild(child)) return true
        }

        return false
    }

    /**
     * @return true if [node] must be ignored
     */
    fun isValidForAccessibility(node: AccessibilityNodeInfoCompat): Boolean {

        return node.isImportantForAccessibility && node.isVisibleToUser
    }

    /**
     * @return true if is mandatory to focus on [node]
     */
    fun mustFocus(node: AccessibilityNodeInfoCompat): Boolean {

        return mustReadContent(node) || mustReadChildren(node)
    }

    /**
     * @return true if is mandatory read [node]'s children
     */
    fun mustReadChildren(node: AccessibilityNodeInfoCompat): Boolean {

        if (mustReadContent(node)) return false

        return isClickable(node) && hasReadableChild(node)
    }

    /**
     * @return true if [node] has content (text or state)
     */
    fun hasContentToRead(node: AccessibilityNodeInfoCompat): Boolean {

        if (node.isCheckable) return true

        if (node.isEditable) return true // TODO: Consider removing after issue #88

        return hasTextToRead(node)
    }

    /**
     * @return true if [node] cannot be read directly
     */
    fun isReadableAsChild(node: AccessibilityNodeInfoCompat): Boolean {

        if (mustFocus(node)) return false

        return hasContentToRead(node) || hasReadableChild(node)
    }
}
