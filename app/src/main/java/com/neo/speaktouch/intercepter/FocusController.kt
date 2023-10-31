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
import com.neo.speaktouch.utils.extension.getNextChildOrNull
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

        runNotNull(target.getNextChildOrNull()) {

            performFocus()

            return
        }

        target.ancestors {
            runNotNull(current.getNextChildOrNull(previous)) {

                performFocus()

                stop = true
            }
        }
    }
}

inline fun <T> runNotNull(
    value: T?,
    block: T.() -> Unit
) {
    if (value != null) {
        block(value)
    }
}

data class AncestorScope(
    val current: AccessibilityNodeInfo,
    val previous: AccessibilityNodeInfo,
) {
    var stop: Boolean = false
}

fun AccessibilityNodeInfo.ancestors(
    block: AncestorScope.() -> Unit,
) {
    var current: AccessibilityNodeInfo? = parent
    var previous: AccessibilityNodeInfo = this

    while (current != null) {

        val scope = AncestorScope(
            current = current,
            previous = previous
        )

        scope.block()

        if (scope.stop) return

        previous = current
        current = previous.parent
    }
}