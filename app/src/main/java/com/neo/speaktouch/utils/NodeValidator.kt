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
import com.neo.speaktouch.model.Type
import com.neo.speaktouch.utils.extension.isNotNullOrEmpty
import com.neo.speaktouch.utils.extension.iterator

object NodeValidator {

    /**
     * @return true if [node] has any click
     */
    fun isClickable(node: AccessibilityNodeInfoCompat): Boolean {

        return node.isClickable || node.isLongClickable
    }

    /**
     * @return true if [node] has any interaction
     */
    fun hasInteraction(node: AccessibilityNodeInfoCompat): Boolean {

        return isClickable(node) || node.isFocusable
    }

    /**
     * @return true if [node] has some text to read
     */
    fun hasTextToRead(node: AccessibilityNodeInfoCompat): Boolean {

        // TODO: Replace with node.getContent().isNotNullOrEmpty()
        return listOf(
            node.text,
            node.hintText?.takeIf { node.isEditable },
            node.contentDescription?.takeIf { !node.isEditable }
        ).any { it.isNotNullOrEmpty() }
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

        if (!isValidForAccessibility(node)) return false

        return mustReadContent(node) || mustReadChildren(node)
    }

    /**
     * @return true if [node] should be read directly
     */
    fun mustReadContent(node: AccessibilityNodeInfoCompat): Boolean {

        return hasReadableContent(node) && hasInteraction(node)
    }

    /**
     * @return true if is mandatory read [node]'s children
     */
    fun mustReadChildren(node: AccessibilityNodeInfoCompat): Boolean {

        if (mustReadContent(node)) return false

        return isClickable(node) && hasReadableChild(node)
    }

    /**
     * @return true if [node] has a readable child
     */
    fun hasReadableChild(node: AccessibilityNodeInfoCompat): Boolean {

        if (!isValidForAccessibility(node)) return false

        for (child in node) {
            if (isReadableAsChild(child)) return true
        }

        return false
    }

    /**
     * @return true if [node] has content (text or state)
     */
    fun hasReadableContent(node: AccessibilityNodeInfoCompat): Boolean {

        if (!isValidForAccessibility(node)) return false

        if (node.isCheckable) return true

        // TODO: Consider removing after issue #88
        if (node.isEditable) return true

        return hasTextToRead(node)
    }

    /**
     * @return true if [node] cannot be read directly
     */
    fun isReadableAsChild(node: AccessibilityNodeInfoCompat): Boolean {

        if (!isValidForAccessibility(node)) return false

        if (mustFocus(node)) return false

        return hasReadableContent(node) ||
                hasReadableChild(node) ||
                Type.get(node) == Type.Button
    }
}
