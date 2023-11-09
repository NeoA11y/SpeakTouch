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

package com.neo.speaktouch.controller

import android.view.accessibility.AccessibilityNodeInfo
import com.neo.speaktouch.model.NodeFilter
import com.neo.speaktouch.utils.extension.Direction
import com.neo.speaktouch.utils.extension.getFocusedOrNull
import com.neo.speaktouch.utils.extension.performFocus
import com.neo.speaktouch.utils.extension.nodeScan

class FocusController(
    private val a11yNodeInfoRoot: () -> AccessibilityNodeInfo
) {

    private val focusedA11yNodeInfo get() = a11yNodeInfoRoot().getFocusedOrNull()

    fun getTarget() = focusedA11yNodeInfo ?: a11yNodeInfoRoot()

    fun moveFocusToPrevious(
        target: AccessibilityNodeInfo = getTarget(),
        nodeFilter: NodeFilter = NodeFilter.Focusable
    ) = nodeScan {

        target.ancestors {

            current.descendants(
                Direction.Left(
                    start = leftIndexOfChild()
                )
            ) {

                recursive()

                if (nodeFilter.filter(current)) {
                    current.performFocus()
                }
            }

            if (nodeFilter.filter(current)) {
                current.performFocus()
            }

            if (current.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)) {

                current.refresh()
                child.refresh()

                repeat()
            }
        }
    }

    fun moveFocusToNext(
        target: AccessibilityNodeInfo = getTarget(),
        nodeFilter: NodeFilter = NodeFilter.Focusable
    ) = nodeScan {

        target.descendants(Direction.Right()) {

            if (nodeFilter.filter(current)) {
                current.performFocus()
            }

            recursive()
        }

        target.ancestors {

            current.descendants(
                Direction.Right(
                    start = rightIndexOfChild()
                )
            ) {

                if (nodeFilter.filter(current)) {
                    current.performFocus()
                }

                recursive()
            }

            if (current.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {

                current.refresh()
                child.refresh()

                repeat()
            }
        }
    }
}