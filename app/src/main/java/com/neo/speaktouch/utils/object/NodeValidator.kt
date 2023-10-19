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

    /**
     * @return true if [node] has any action
     */
    private fun isClickable(node: NodeInfo): Boolean {

        return node.isClickable || node.isLongClickable
    }

    /**
     * @return true if [node] has some text to read
     */
    private fun hasTextToRead(node: NodeInfo): Boolean {

        return listOf(
            node.text,
            node.hintText,
            node.contentDescription
        ).any { it.isNotNullOrEmpty() }
    }

    /**
     * @return true if [node] should be read directly
     */
    private fun mustReadContent(node: NodeInfo): Boolean {

        return hasContentToRead(node) && node.isFocusable
    }

    /**
     * @return true if [node] has a readable child
     */
    private fun hasReadableChild(node: NodeInfo): Boolean {

        for (child in node) {
            if (!isValidForAccessibility(child)) continue
            if (isReadableAsChild(child)) return true
        }

        return false
    }

    /**
     * @return true if [node] must be ignored
     */
    fun isValidForAccessibility(node: NodeInfo): Boolean {

        return node.isImportantForAccessibility && node.isVisibleToUser
    }

    /**
     * @return true if is mandatory to focus on [node]
     */
    fun mustFocus(node: NodeInfo): Boolean {

        return mustReadContent(node) || mustReadChildren(node)
    }

    /**
     * @return true if is mandatory read [node]'s children
     */
    fun mustReadChildren(node: NodeInfo) : Boolean {

        if (mustReadContent(node)) return false

        return isClickable(node) && hasReadableChild(node)
    }

    /**
     * @return true if [node] has content (text or state)
     */
    fun hasContentToRead(node: NodeInfo): Boolean {

        return hasTextToRead(node) || node.isCheckable
    }

    /**
     * @return true if [node] cannot be read directly
     */
    fun isReadableAsChild(node: NodeInfo): Boolean {

        if (mustFocus(node)) return false

        return hasContentToRead(node) || hasReadableChild(node)
    }
}