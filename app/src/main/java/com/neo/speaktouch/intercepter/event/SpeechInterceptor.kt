/*
 * Speak focused content.
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

package com.neo.speaktouch.intercepter.event

import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.controller.SpeechController
import com.neo.speaktouch.intercepter.event.contract.EventInterceptor
import com.neo.speaktouch.utils.extension.isAccessibilityFocused
import com.neo.speaktouch.utils.extension.isTouchInteractionStart
import dagger.hilt.android.scopes.ServiceScoped
import timber.log.Timber
import javax.inject.Inject

@ServiceScoped
class SpeechInterceptor @Inject constructor(
    private val speech: SpeechController
) : EventInterceptor {

    override fun handle(event: AccessibilityEvent) {

        if (event.isTouchInteractionStart && speech.isSpeaking) {

            speech.stop()

            return
        }

        if (event.isAccessibilityFocused) {
            speech.speak(AccessibilityNodeInfoCompat.wrap(event.source ?: return))
        }
    }

    override fun finish() {

        Timber.i("finish")

        speech.shutdown()
    }
}
