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

fun nodeScan(block: NodeScan.() -> Unit) {
    try {
        NodeScan().block()
    } catch (_: NodeScanStopException) {
        // success finish
    }
}

open class NodeScan {

    fun AccessibilityNodeInfo.ancestors(
        block: NodeScanScope.() -> Unit
    ) {
        var scope = NodeScanScope.Ancestor(
            current = parent ?: return,
            previous = this
        )

        while (true) {

            scope.block()

            val current = scope.current
            val parent = current.parent ?: return

            scope = NodeScanScope.Ancestor(
                current = parent,
                previous = current
            )
        }
    }

    fun AccessibilityNodeInfo.descendants(
        direction: Direction,
        block: NodeScanScope.Descendant.() -> Unit
    ) {
        val range = when (direction) {
            is Direction.Previous -> direction.start downTo 0
            is Direction.Next -> direction.start..lastIndex
        }

        for (index in range) {

            val child = getChild(index) ?: continue

            NodeScanScope.Descendant(
                current = child,
                previous = this,
                recursive = {
                    child.descendants(
                        block = block,
                        direction = when (direction) {
                            is Direction.Previous -> {
                                Direction.Previous(start = child.lastIndex)
                            }

                            is Direction.Next -> {
                                Direction.Next(start = 0)
                            }
                        }
                    )
                }
            ).block()
        }
    }
}

sealed class NodeScanScope : NodeScan() {

    abstract val current: AccessibilityNodeInfo
    abstract val previous: AccessibilityNodeInfo

    class Ancestor(
        override val current: AccessibilityNodeInfo,
        override val previous: AccessibilityNodeInfo
    ) : NodeScanScope()

    class Descendant(
        override val current: AccessibilityNodeInfo,
        override val previous: AccessibilityNodeInfo,
        val recursive: () -> Unit = {}
    ) : NodeScanScope()

    fun stop() {
        throw NodeScanStopException()
    }
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

class NodeScanStopException : Exception()
