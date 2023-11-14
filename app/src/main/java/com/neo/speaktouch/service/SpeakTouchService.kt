/*
 * Speak Touch accessibility service.
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

package com.neo.speaktouch.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.event.EventInterceptor
import com.neo.speaktouch.intercepter.event.FocusInterceptor
import com.neo.speaktouch.intercepter.event.GestureInterceptor
import com.neo.speaktouch.intercepter.event.HapticInterceptor
import com.neo.speaktouch.intercepter.event.SpeechInterceptor
import com.neo.speaktouch.intercepter.gesture.CallbackInterceptor
import com.neo.speaktouch.utils.extension.addFlags
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class SpeakTouchService : AccessibilityService() {

    init {
        context = WeakReference(this)
    }

    @Inject
    lateinit var gestureInterceptor: GestureInterceptor

    @Inject
    lateinit var focusInterceptor: FocusInterceptor

    @Inject
    lateinit var callbackInterceptor: CallbackInterceptor

    @Inject
    lateinit var speechInterceptor: SpeechInterceptor

    @Inject
    lateinit var hapticInterceptor: HapticInterceptor

    private val eventInterceptors = mutableListOf<EventInterceptor>()

    override fun onCreate() {
        super.onCreate()

        setupInterceptors()
    }

    private fun setupInterceptors() {

        eventInterceptors.add(speechInterceptor)

        eventInterceptors.add(callbackInterceptor)

        eventInterceptors.add(focusInterceptor)

        eventInterceptors.add(hapticInterceptor)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addFlags(AccessibilityServiceInfo.FLAG_REQUEST_2_FINGER_PASSTHROUGH)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        eventInterceptors.forEach {
            it.handle(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        eventInterceptors.forEach(EventInterceptor::finish)
        eventInterceptors.clear()
    }

    override fun onInterrupt() = Unit

    @Deprecated("Deprecated in Java")
    override fun onGesture(gestureId: Int): Boolean {
        return gestureInterceptor.handle(gestureId)
    }

    companion object {
        lateinit var context: WeakReference<SpeakTouchService>
    }
}