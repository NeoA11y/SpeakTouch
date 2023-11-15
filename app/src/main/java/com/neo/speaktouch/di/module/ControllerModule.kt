/*
 * Controllers DI module.
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

package com.neo.speaktouch.di.module

import android.content.Context
import android.media.AudioAttributes
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import com.neo.speaktouch.R
import com.neo.speaktouch.controller.Controllers
import com.neo.speaktouch.controller.SpeechController
import com.neo.speaktouch.controller.VibratorController
import com.neo.speaktouch.model.Reader
import com.neo.speaktouch.model.UiText
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ControllerModule {

    @Provides
    @ServiceScoped
    fun providesVibratorController(
        context: Context
    ) : VibratorController {

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        return VibratorController(vibrator)
    }

    @Provides
    @ServiceScoped
    fun providesSpeechController(
        context: Context,
    ): SpeechController {

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Controllers.speech.speak(
                    UiText(R.string.speak_touch_activated)
                )
            }
        }

        textToSpeech.setAudioAttributes(audioAttributes)

        return SpeechController(textToSpeech, context, Reader(context))
    }
}
