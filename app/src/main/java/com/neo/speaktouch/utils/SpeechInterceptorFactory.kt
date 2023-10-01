/*
 * Create and configure a SpeechInterceptor.
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

package com.neo.speaktouch.utils

import android.content.Context
import android.media.AudioAttributes
import android.speech.tts.TextToSpeech
import com.neo.speaktouch.R
import com.neo.speaktouch.intercepter.SpeechInterceptor
import com.neo.speaktouch.model.Reader
import com.neo.speaktouch.model.UiText

object SpeechInterceptorFactory {

    fun create(context: Context): SpeechInterceptor {

        var speechInterceptor: SpeechInterceptor? = null

        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                speechInterceptor!!.speak(
                    UiText(
                        text = "%s %s",
                        UiText(R.string.app_name),
                        UiText(R.string.text_enabled)
                    ),
                )
            }
        }

        textToSpeech.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        )

        speechInterceptor = SpeechInterceptor(
            tts = textToSpeech,
            reader = Reader(context),
            context = context
        )

        return speechInterceptor
    }
}