/*
 * Speak focused content.
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

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import com.neo.speaktouch.intercepter.interfece.Interceptor
import com.neo.speaktouch.model.Reader
import com.neo.speaktouch.model.UiText
import com.neo.speaktouch.utils.extension.getLog
import com.neo.speaktouch.utils.`typealias`.NodeInfo
import timber.log.Timber

class SpeechInterceptor(
    private val tts: TextToSpeech,
    private val context: Context,
    private val reader: Reader
) : Interceptor {

    override fun handle(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            speak(NodeInfo.wrap(event.source ?: return))
        }
    }

    fun speak(text: CharSequence) {

        Timber.i("speak:  \"$text\"")

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    fun speak(text: UiText) {
        Timber.i("speak: $text")

        speak(text.resolved(context))
    }

    private fun speak(nodeInfo: NodeInfo) {

        Timber.i("speak: ${nodeInfo.getLog()}")

        speak(reader.getContent(nodeInfo))
    }

    fun shutdown() {

        Timber.i("shutdown")

        tts.shutdown()
    }
}