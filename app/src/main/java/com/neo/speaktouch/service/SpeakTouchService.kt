/*
 * Speak Touch accessibility service.
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

package com.neo.speaktouch.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.controller.FocusController
import com.neo.speaktouch.intercepter.FocusInterceptor
import com.neo.speaktouch.intercepter.GestureInterceptor
import com.neo.speaktouch.intercepter.HapticInterceptor
import com.neo.speaktouch.intercepter.SpeechInterceptor
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.utils.extension.getLog
import com.neo.speaktouch.utils.VibrationUtil
import timber.log.Timber

class SpeakTouchService : AccessibilityService() {

    private val interceptors = mutableListOf<Interceptor>()

    private lateinit var gestureInterceptor: GestureInterceptor

    override fun onCreate() {
        super.onCreate()

        gestureInterceptor = GestureInterceptor(
            FocusController { rootInActiveWindow }
        )

        interceptors.add(FocusInterceptor())

        interceptors.add(SpeechInterceptor.getInstance(this))

        interceptors.add(
            HapticInterceptor(
                vibration = VibrationUtil(this),
            )
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(event.getLog())

        interceptors.forEach {
            it.handle(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        interceptors.forEach(Interceptor::finish)
        interceptors.clear()
    }

    override fun onInterrupt() = Unit

    @Deprecated("Deprecated in Java")
    override fun onGesture(gestureId: Int): Boolean {
        return gestureInterceptor.handle(gestureId)
    }
}

