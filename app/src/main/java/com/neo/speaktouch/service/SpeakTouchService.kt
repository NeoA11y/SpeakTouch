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
import com.neo.speaktouch.controller.Controllers
import com.neo.speaktouch.intercepter.Interceptors
import com.neo.speaktouch.intercepter.event.contract.EventInterceptor
import com.neo.speaktouch.utils.extension.addFlags
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SpeakTouchService : AccessibilityService() {

    @Inject
    lateinit var interceptors: Interceptors

    override fun onCreate() {
        super.onCreate()

        Controllers.install()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addFlags(AccessibilityServiceInfo.FLAG_REQUEST_2_FINGER_PASSTHROUGH)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        interceptors.event.forEach {
            it.handle(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        interceptors.event.forEach(EventInterceptor::finish)

        Controllers.uninstall()
    }

    override fun onInterrupt() = Unit

    @Deprecated("Deprecated in Java")
    override fun onGesture(gestureId: Int): Boolean {
        return interceptors.gesture.handle(gestureId)
    }
}
