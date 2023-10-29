/*
 * Focus controller.
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

import android.view.accessibility.AccessibilityNodeInfo
import com.neo.speaktouch.utils.extension.getFocusedOrNull
import com.neo.speaktouch.utils.extension.getLastOrNull
import com.neo.speaktouch.utils.extension.getNextOrNull
import com.neo.speaktouch.utils.extension.getPreviousOrNull
import com.neo.speaktouch.utils.extension.performFocus

class FocusController(
    private val a11yNodeInfoRoot: () -> AccessibilityNodeInfo
) {

    val focusedA11yNodeInfo get() = a11yNodeInfoRoot().getFocusedOrNull()

    fun focusPrevious() {
        val target = focusedA11yNodeInfo ?: a11yNodeInfoRoot()

        val parent = target.parent ?: return

        // focus in the previous element
        parent.getPreviousOrNull(target)?.run {

            if (childCount == 0) {
                performFocus()
            } else {
                getLastOrNull()?.performFocus()
            }

            return
        }

        // focus in the parent
        parent.performFocus()
    }

    fun focusNext() {
        val target = focusedA11yNodeInfo ?: a11yNodeInfoRoot()

        // focus in the next child of the target
        target.getNextOrNull()?.run {

            performFocus()

            return
        }

        // focus in the next element from the parent
        target.parent?.getNextOrNull(target)?.run {

            performFocus()

            return
        }

        // focus in the next parent
        target.parent?.parent?.getNextOrNull(target.parent)?.performFocus()
    }
}