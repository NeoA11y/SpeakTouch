/*
 * Speech Controller.
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

package com.neo.speaktouch.controller

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.model.Text
import com.neo.speaktouch.utils.Reader
import com.neo.speaktouch.utils.Node
import timber.log.Timber

class SpeechController(
    private val textToSpeech: TextToSpeech,
    private val context: Context,
    private val reader: Reader
) {
    val isSpeaking: Boolean get() = textToSpeech.isSpeaking

    private fun speak(text: CharSequence) {

        Timber.i("speak:  \"$text\"")

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    fun speak(text: Text) {
        speak(text.resolved(context))
    }

    fun speak(nodeInfo: AccessibilityNodeInfoCompat) {

        Timber.d("node: ${Node(nodeInfo).geJson().toString(4)}")

        speak(reader.read(nodeInfo))
    }

    fun stop() {
        textToSpeech.stop()
    }

    fun shutdown() {
        textToSpeech.shutdown()
    }
}
