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
import android.view.accessibility.AccessibilityNodeInfo
import com.neo.speaktouch.intercepter.FocusInterceptor
import com.neo.speaktouch.intercepter.SpeechInterceptor
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.utils.extension.focus
import com.neo.speaktouch.utils.extension.getInstance
import com.neo.speaktouch.utils.extension.getLog
import timber.log.Timber

class SpeakTouchService : AccessibilityService() {

    private val interceptors = mutableListOf<Interceptor>()

    override fun onCreate() {
        super.onCreate()

        interceptors.add(FocusInterceptor())
        interceptors.add(SpeechInterceptor.getInstance(this))
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        Timber.d(event.getLog())

        interceptors.forEach { it.handle(event) }
    }

    override fun onDestroy() {
        super.onDestroy()

        interceptors
            .getInstance<SpeechInterceptor>()
            .shutdown()

        interceptors.clear()
    }

    override fun onInterrupt() = Unit

    @Deprecated("Deprecated in Java")
    override fun onGesture(gestureId: Int): Boolean {

        if (gestureId == GESTURE_SWIPE_UP) {

            return true
        }

        if (gestureId == GESTURE_SWIPE_DOWN) {
            moveFocusToNext()
            return true
        }

        return false
    }

    private fun moveFocusToNext() {

        val target = findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY) ?: rootInActiveWindow

        // focus in the next child of the target
        target.getNextOrNull()?.run {
            focus()

            return
        }

        // focus in the next element from the parent
        target.parent?.getNextOrNull(target)?.run {
            focus()

            return
        }

        // focus in the next parent
        target.parent?.parent?.getNextOrNull(target.parent)?.focus()
    }
}

private fun AccessibilityNodeInfo.getNextOrNull(
    current: AccessibilityNodeInfo? = null
): AccessibilityNodeInfo? {

    if (childCount == 0) return null

    if (current == null) return getChild(0)

    val currentIndex = indexOfChild(current)

    if (childCount > currentIndex + 1) return getChild(currentIndex + 1)

    return null
}

private fun AccessibilityNodeInfo.indexOfChild(
    current: AccessibilityNodeInfo
): Int {

    for (index in 0 until childCount) {

        val child = getChild(index)

        if (child == current) {
            return index
        }
    }

    error("Child not found")
}
