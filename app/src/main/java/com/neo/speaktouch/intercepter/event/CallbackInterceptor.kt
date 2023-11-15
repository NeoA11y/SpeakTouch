/*
 * Callbacks interceptor.
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

package com.neo.speaktouch.intercepter.gesture

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.neo.speaktouch.intercepter.event.EventInterceptor
import javax.inject.Inject

@ServiceScoped
class CallbackInterceptor @Inject constructor() : EventInterceptor {

    private val scrolls = mutableListOf<Scroll>()

    @Synchronized
    override fun handle(event: AccessibilityEvent) {

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {

            scrolls.lastOrNull {
                it.target == event.source
            }?.let { scroll ->

                scrolls.remove(scroll)
                scroll.invoke()
            }
        }
    }

    fun addCallback(callback: Scroll) {
        scrolls.add(callback)
    }

}

abstract class Scroll(val target: AccessibilityNodeInfo) {
    abstract operator fun invoke()
}
