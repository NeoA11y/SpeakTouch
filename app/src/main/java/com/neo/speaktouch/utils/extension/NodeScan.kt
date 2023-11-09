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

/**
 * Open a nodes scan block.
 * @see NodeScan
 */
fun nodeScan(block: NodeScan.() -> Unit) {
    try {
        NodeScan().block()
    } catch (_: NodeScanStop) {
        // success finish
    }
}

open class NodeScan {

    /**
     * Scan ancestral nodes.
     * Call [NodeScanScope.Ancestor.stop] to stop scanning.
     * @see NodeScanScope.Ancestor
     */
    fun AccessibilityNodeInfo.ancestors(
        block: NodeScanScope.Ancestor.() -> Unit
    ) {
        var scope = NodeScanScope.Ancestor(
            current = parent ?: return,
            child = this
        )

        while (true) {

            try {
                scope.block()
            } catch (_: NodeScanRepeat) {
                continue
            }

            val current = scope.current
            val parent = current.parent ?: return

            scope = NodeScanScope.Ancestor(
                current = parent,
                child = current
            )
        }
    }

    /**
     * Scan descendant nodes.
     * Call [NodeScanScope.Descendant.recursive] to scan deeply.
     * Call [NodeScanScope.Descendant.stop] to stop scanning.
     * @see NodeScanScope.Descendant
     */
    fun AccessibilityNodeInfo.descendants(
        direction: Direction,
        block: NodeScanScope.Descendant.() -> Unit
    ) {
        val range = when (direction) {
            is Direction.Left -> direction.start downTo 0
            is Direction.Right -> direction.start..lastIndex
        }

        for (index in range) {

            val child = getChild(index) ?: continue

            NodeScanScope.Descendant(
                current = child,
                parent = this,
                recursive = {
                    child.descendants(
                        block = block,
                        direction = when (direction) {
                            is Direction.Left -> {
                                Direction.Left(child.lastIndex)
                            }

                            is Direction.Right -> {
                                Direction.Right(start = 0)
                            }
                        }
                    )
                }
            ).block()
        }
    }
}

sealed class NodeScanScope : NodeScan() {

    /**
     * Ancestor node scope.
     * @property current current node
     * @property child node from which this ancestor was accessed
     */
    class Ancestor(
        val current: AccessibilityNodeInfo,
        val child: AccessibilityNodeInfo
    ) : NodeScanScope() {

        fun indexOfChild() = current.indexOfChild(child)
        fun leftIndexOfChild() = indexOfChild().dec()
        fun rightIndexOfChild() = indexOfChild().inc()

        fun repeat() {
            throw NodeScanRepeat()
        }
    }

    /**
     * Descendant node scope.
     * @property current current node
     * @property parent node from which this descendant was accessed
     * @property recursive call this function to scan deeply
     */
    class Descendant(
        val current: AccessibilityNodeInfo,
        val parent: AccessibilityNodeInfo,
        val recursive: () -> Unit = {}
    ) : NodeScanScope()

    fun stop() {
        throw NodeScanStop()
    }
}

/**
 * Scan direction.
 */
sealed class Direction {

    abstract val start: Int

    data class Left(
        override val start: Int
    ) : Direction()

    data class Right(
        override val start: Int = 0
    ) : Direction()
}

class NodeScanStop : Exception()
class NodeScanRepeat : Exception()
