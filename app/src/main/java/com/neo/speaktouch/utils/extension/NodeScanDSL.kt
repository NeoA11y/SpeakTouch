/*
 * Node Scan DSL.
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

package com.neo.speaktouch.utils.extension

import android.view.accessibility.AccessibilityNodeInfo

sealed class Direction {

    abstract val start: Int

    data class Previous(
        override val start: Int
    ) : Direction()

    data class Next(
        override val start: Int
    ) : Direction()
}

data class ScanScope(
    val current: AccessibilityNodeInfo,
    val previous: AccessibilityNodeInfo,
) {
    var stop: Boolean = false

    fun ifRunning(block: () -> Unit) {
        if (stop) return
        block()
    }
}

// descendants

context(ScanScope)
fun AccessibilityNodeInfo.descendants(
    direction: Direction,
    block: ScanScope.() -> Unit,
): Boolean {
    stop = internalDescendants(
        direction = direction,
        block = block
    )

    return stop
}

fun AccessibilityNodeInfo.descendants(
    direction: Direction,
    block: ScanScope.() -> Unit,
) = internalDescendants(
    direction = direction,
    block = block
)

private fun AccessibilityNodeInfo.internalDescendants(
    direction: Direction,
    block: ScanScope.() -> Unit,
): Boolean {

    val range = when (direction) {
        is Direction.Previous -> direction.start downTo 0
        is Direction.Next -> direction.start..lastIndex
    }

    for (index in range) {

        val child = getChild(index) ?: continue

        val scope = ScanScope(
            current = child,
            previous = this
        )

        scope.block()

        if (scope.stop) return true

        val result = child.internalDescendants(
            block = block,
            direction = when (direction) {
                is Direction.Previous -> Direction.Previous(start = lastIndex)
                is Direction.Next -> Direction.Next(start = 0)
            }
        )

        if (result) return true
    }

    return false
}

// ancestors

context(ScanScope)
fun AccessibilityNodeInfo.ancestors(
    block: ScanScope.() -> Unit
): Boolean {
    stop = internalAncestors(block)

    return stop
}

fun AccessibilityNodeInfo.ancestors(
    block: ScanScope.() -> Unit
) = internalAncestors(block)

private fun AccessibilityNodeInfo.internalAncestors(
    block: ScanScope.() -> Unit
): Boolean {

    var scope = ScanScope(
        current = parent ?: return false,
        previous = this
    )

    while (true) {

        scope.block()

        if (scope.stop) return true

        val current = scope.current

        scope = ScanScope(
            current = current.parent ?: return false,
            previous = current
        )
    }
}