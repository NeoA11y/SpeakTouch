/*
 * Intercepts user gestures.
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

import android.accessibilityservice.AccessibilityService

class GestureInterceptor(
    private val focusController: FocusController
) {

    fun handle(gestureId: Int): Boolean {
        if (gestureId == AccessibilityService.GESTURE_SWIPE_UP) {
            focusController.focusPrevious()
            return true
        }

        if (gestureId == AccessibilityService.GESTURE_SWIPE_DOWN) {
            focusController.focusNext()
            return true
        }

        return false
    }
}