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
import com.neo.speaktouch.utils.extension.indexOfChild
import com.neo.speaktouch.utils.extension.performFocus
import java.util.Stack

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

        val result = target.descendants {

            current.performFocus()

            stop = true
        }

        if (result) return

        target.ancestors {

            current.descendants {

                current.performFocus()

                this@ancestors.stop = true
                this@descendants.stop = true
            }
        }
    }
}

data class DescendantsScope(
    val current: AccessibilityNodeInfo,
    val previous: AccessibilityNodeInfo,
) {
    var stop: Boolean = false
}

fun AccessibilityNodeInfo.descendants(
    startIndex: Int = 0,
    block: DescendantsScope.() -> Unit,
): Boolean {

    for (index in startIndex until childCount) {

        val child = getChildOrNull(index) ?: continue

        val scope = DescendantsScope(
            current = child,
            previous = this
        )

        scope.block()

        if (scope.stop) return true

        child.descendants(block = block)
    }

    return false
}

fun AccessibilityNodeInfo.getChildOrNull(
    index: Int
) = runCatching {
    getChild(index)
}.getOrNull()

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

    fun AccessibilityNodeInfo.descendants(
        block: DescendantsScope.() -> Unit
    ) = descendants(
        startIndex = indexOfChild(previous) + 1,
        block = block
    )
}

inline fun AccessibilityNodeInfo.ancestors(
    block: AncestorScope.() -> Unit
): Boolean {

    var scope = AncestorScope(
        current = parent ?: return false,
        previous = this
    )

    while (true) {

        scope.block()

        if (scope.stop) return true

        val current = scope.current

        scope = AncestorScope(
            current = current.parent ?: return false,
            previous = current
        )
    }
}