/*
 * All interceptors.
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

import com.neo.speaktouch.intercepter.event.CallbackInterceptor
import com.neo.speaktouch.intercepter.event.FocusInterceptor
import com.neo.speaktouch.intercepter.gesture.GestureInterceptor
import com.neo.speaktouch.intercepter.event.HapticInterceptor
import com.neo.speaktouch.intercepter.event.SpeechInterceptor
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class Interceptors @Inject constructor(
    val speech: SpeechInterceptor,
    val focus: FocusInterceptor,
    val haptic: HapticInterceptor,
    val callback : CallbackInterceptor,
    val gesture: GestureInterceptor
) {
    val event = listOf(speech, focus, haptic, callback)
}