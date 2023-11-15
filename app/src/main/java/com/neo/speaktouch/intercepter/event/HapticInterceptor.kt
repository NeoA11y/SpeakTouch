/*
 * Handle haptic feedback.
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

package com.neo.speaktouch.intercepter.event

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.neo.speaktouch.controller.VibratorController
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class HapticInterceptor @Inject constructor(
    private val vibration: VibratorController
) : EventInterceptor {

    override fun handle(event: AccessibilityEvent) {

        if (!vibration.hasVibrator()) return

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            handleFocusVibration(event.source ?: return)
        }
    }

    private fun handleFocusVibration(nodeInfo: AccessibilityNodeInfo) {

        if (nodeInfo.isClickable) {
            vibration.vibrateEffectHeavyClick()
            return
        }

        vibration.vibrateEffectTick()

        return
    }

    override fun finish() = vibration.finish()
}
