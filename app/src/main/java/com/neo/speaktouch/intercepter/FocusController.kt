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
import com.neo.speaktouch.utils.extension.Direction
import com.neo.speaktouch.utils.extension.ancestors
import com.neo.speaktouch.utils.extension.descendants
import com.neo.speaktouch.utils.extension.getFocusedOrNull
import com.neo.speaktouch.utils.extension.getLastOrNull
import com.neo.speaktouch.utils.extension.getPreviousOrNull
import com.neo.speaktouch.utils.extension.indexOfChild
import com.neo.speaktouch.utils.extension.lastIndex
import com.neo.speaktouch.utils.extension.performFocus

class FocusController(
    private val a11yNodeInfoRoot: () -> AccessibilityNodeInfo
) {

    val focusedA11yNodeInfo get() = a11yNodeInfoRoot().getFocusedOrNull()

    fun focusPrevious() {
        val target = focusedA11yNodeInfo ?: a11yNodeInfoRoot()

        target.ancestors {

            current.descendants(
                Direction.Previous(
                    start = current.indexOfChild(previous) - 1
                )
            ) {

                current.descendants(
                    Direction.Previous(start = current.lastIndex)
                ) {
                    current.performFocus()

                    stop = true
                }

                if (stop) return@descendants

                current.performFocus()

                stop = true
            }

            if (stop) return@ancestors

            current.performFocus()

            stop = true
        }
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

                stop = true
            }
        }
    }
}