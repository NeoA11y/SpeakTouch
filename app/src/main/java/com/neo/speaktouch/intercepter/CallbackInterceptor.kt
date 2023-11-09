/*
 * Global callbacks interceptor.
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

import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.interfece.Interceptor
import java.util.Stack

object CallbackInterceptor : Interceptor {

    private val scroll = Stack<Scroll>()

    override fun handle(event: AccessibilityEvent) {

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {

            if (scroll.isNotEmpty()) {
                scroll.pop().invoke()
            }
        }
    }

    fun addCallback(callback: Scroll) {
        scroll.push(callback)
    }

}

fun interface Scroll {
    operator fun invoke()
}