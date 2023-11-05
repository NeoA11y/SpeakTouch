/*
 * Intercepts user gestures.
 *
 * Copyright (C) 2023 Irineu A. Silva.
 * Copyright (C) 2023 Patryk Miś.
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
import android.os.Build
import com.neo.speaktouch.controller.FocusController

class GestureInterceptor(
    private val focusController: FocusController,
    private val accessibilityService: AccessibilityService
) {

    fun handle(gestureId: Int): Boolean {

        if (gestureId == AccessibilityService.GESTURE_SWIPE_LEFT) {
            focusController.moveFocusToPrevious()
            return true
        }

        if (gestureId == AccessibilityService.GESTURE_SWIPE_RIGHT) {
            focusController.moveFocusToNext()
            return true
        }

        if (gestureId == AccessibilityService.GESTURE_SWIPE_DOWN_AND_LEFT) {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            return true
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return false

// Android 12+ gestures

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && gestureId == AccessibilityService.GESTURE_2_FINGER_DOUBLE_TAP) {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_KEYCODE_HEADSETHOOK)
            return true
        }

        return false
    }
}
