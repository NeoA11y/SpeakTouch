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
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.model.NodeFilter
import com.neo.speaktouch.utils.extension.Direction
import com.neo.speaktouch.utils.extension.ancestors
import com.neo.speaktouch.utils.extension.descendants
import com.neo.speaktouch.utils.extension.getFocusedOrNull
import com.neo.speaktouch.utils.extension.getNearestAncestor
import com.neo.speaktouch.utils.extension.indexOfChild
import com.neo.speaktouch.utils.extension.lastIndex
import com.neo.speaktouch.utils.extension.performFocus
import com.neo.speaktouch.utils.`object`.NodeValidator

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
                    if (NodeFilter.Focusable.filter(current)) {
                        current.performFocus()
                    }
                }

                ifRunning {
                    if (NodeFilter.Focusable.filter(current)) {
                        current.performFocus()
                    }
                }
            }

            ifRunning {
                if (NodeFilter.Focusable.filter(current)) {
                    current.performFocus()
                }
            }
        }
    }

    fun focusNext() {
        val target = focusedA11yNodeInfo ?: a11yNodeInfoRoot()

        target.descendants(
            Direction.Next(start = 0)
        ) {
            if (NodeFilter.Focusable.filter(current)) {
                current.performFocus()
            }
        }.also {
            if (it) return
        }

        target.ancestors {

            current.descendants(
                Direction.Next(
                    start = current.indexOfChild(previous) + 1
                )
            ) {
                if (NodeFilter.Focusable.filter(current)) {
                    current.performFocus()
                }
            }
        }
    }
}