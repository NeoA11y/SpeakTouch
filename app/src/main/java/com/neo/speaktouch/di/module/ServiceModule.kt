/*
 * Services DI module.
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

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Context
import android.os.Vibrator
import com.neo.speaktouch.service.SpeakTouchService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

object ServiceModule {

    @Module
    @InstallIn(ServiceComponent::class)
    object Provide {

        @Provides
        @ServiceScoped
        fun providesAccessibilityService(
            service: Service
        ): AccessibilityService {

            if (service !is AccessibilityService) {
                error("Service must be an AccessibilityService")
            }

            return service
        }

        @Provides
        @ServiceScoped
        fun providesSpeakTouchService(
            service: Service
        ): SpeakTouchService {

            if (service !is SpeakTouchService) {
                error("Service must be an SpeakTouchService")
            }

            return service
        }
    }

    @Module
    @InstallIn(ServiceComponent::class)
    abstract class Bind {

        @Binds
        @ServiceScoped
        abstract fun providesContext(
            service: Service
        ): Context
    }
}
