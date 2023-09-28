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
    private fun hasText(node: NodeInfo): Boolean {
        return listOf(
            node.text,
            node.hintText,
            node.contentDescription
        ).any { it.isNotNullOrEmpty() }
    }

    /**
     * @return true if [node] should be read directly
     */
    private fun isRequiredRead(node: NodeInfo): Boolean {
        return node.isFocusable && isReadable(node)
    }

    /**
     * @return true if [node] has a readable child
     */
    private fun hasReadableChild(node: NodeInfo): Boolean {

        for (child in node) {
            if (isReadableAsChild(child)) return true
        }

        return false
    }

    /**
     * @return true if [node] should be ignored
     */
    fun isValidForAccessible(node: NodeInfo): Boolean {
        return node.isImportantForAccessibility && node.isVisibleToUser
    }

    /**
     * @return true if it is mandatory to focus on [node]
     */
    fun isRequiredFocus(node: NodeInfo): Boolean {

        if (isRequiredRead(node)) return true

        return isClickable(node) && hasReadableChild(node)
    }

    /**
     * @return true if [node] has some information that can be read, either text or state
     */
    fun isReadable(node: NodeInfo): Boolean {
        return hasText(node) || node.isCheckable
    }

    /**
     * @return true if [node] cannot be read directly
     */
    fun isReadableAsChild(node: NodeInfo): Boolean {
        return isReadable(node) && !isRequiredFocus(node)
    }
}