/*
 * Interceptors DI.
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
import com.neo.speaktouch.controller.FocusController
import com.neo.speaktouch.controller.ServiceController
import com.neo.speaktouch.controller.SpeechController
import com.neo.speaktouch.controller.VibratorController
import com.neo.speaktouch.intercepter.event.FocusInterceptor
import com.neo.speaktouch.intercepter.event.GestureInterceptor
import com.neo.speaktouch.intercepter.event.HapticInterceptor
import com.neo.speaktouch.intercepter.event.SpeechInterceptor
import com.neo.speaktouch.intercepter.gesture.CallbackInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object InterceptorModule {

    @Provides
    @ServiceScoped
    fun providesGestureInterceptor(
        focusController: FocusController,
        serviceController: ServiceController
    ): GestureInterceptor {

        return GestureInterceptor(focusController, serviceController)
    }

    @Provides
    @ServiceScoped
    fun providesCallbackInterceptor(): CallbackInterceptor {

        return CallbackInterceptor()
    }

    @Provides
    @ServiceScoped
    fun providesFocusInterceptor(): FocusInterceptor {

        return FocusInterceptor()
    }

    @Provides
    @ServiceScoped
    fun providesHapticInterceptor(
        context: Context
    ): HapticInterceptor {

        val vibratorController = VibratorController(context)

        return HapticInterceptor(vibratorController)
    }

    @Provides
    @ServiceScoped
    fun providesSpeechInterceptor(
        speechController: SpeechController
    ): SpeechInterceptor {

        return SpeechInterceptor(speechController)
    }
}
