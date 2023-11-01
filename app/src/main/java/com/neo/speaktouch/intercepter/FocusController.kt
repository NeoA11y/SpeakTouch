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
import com.neo.speaktouch.utils.extension.getPreviousOrNull
import com.neo.speaktouch.utils.extension.indexOfChild
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

        val result = target.descendants(
            Direction.Next(start = 0)
        ) {

            current.performFocus()

            stop = true
        }

        if (result) return

        target.ancestors {

            val direction = Direction.Next(
                start = current.indexOfChild(previous) + 1
            )

            current.descendants(direction) {

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

sealed class Direction {

    abstract val start: Int

    data class Previous(
        override val start: Int
    ) : Direction()

    data class Next(
        override val start: Int
    ) : Direction()
}

fun AccessibilityNodeInfo.descendants(
    direction: Direction,
    block: DescendantsScope.() -> Unit,
): Boolean {

    val range = when (direction) {
        is Direction.Previous -> direction.start downTo childCount
        is Direction.Next -> direction.start until childCount
    }

    for (index in range) {

        val child = getChildOrNull(index) ?: continue

        val scope = DescendantsScope(
            current = child,
            previous = this
        )

        scope.block()

        if (scope.stop) return true

        child.descendants(
            block = block,
            direction = when (direction) {
                is Direction.Previous -> Direction.Previous(start = childCount)
                is Direction.Next -> Direction.Next(start = 0)
            }
        )
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