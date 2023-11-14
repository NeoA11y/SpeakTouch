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
import com.neo.speaktouch.intercepter.gesture.CallbackInterceptor
import com.neo.speaktouch.intercepter.gesture.Scroll
import com.neo.speaktouch.model.NodeFilter
import com.neo.speaktouch.utils.extension.Direction
import com.neo.speaktouch.utils.extension.performFocus
import com.neo.speaktouch.utils.extension.nodeScan

class FocusController(
    private val callbackInterceptor: CallbackInterceptor,
    private val serviceController: ServiceController
) {

    fun getTarget(): AccessibilityNodeInfo {

        return serviceController.getFocused() ?: serviceController.getRoot()
    }

    fun moveFocusToPrevious(
        target: AccessibilityNodeInfo = getTarget(),
        nodeFilter: NodeFilter = NodeFilter.Focusable
    ) {
        nodeScan {

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

                    callbackInterceptor.addCallback(
                        object : Scroll(current) {
                            override fun invoke() {
                                moveFocusToPrevious()
                            }
                        }
                    )

                    stop()
                }
            }
        }
    }

    fun moveFocusToNext(
        target: AccessibilityNodeInfo = getTarget(),
        nodeFilter: NodeFilter = NodeFilter.Focusable
    ) {
        nodeScan {

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

                    callbackInterceptor.addCallback(
                        object : Scroll(current) {
                            override fun invoke() {
                                moveFocusToNext()
                            }
                        }
                    )

                    stop()
                }
            }
        }
    }
}