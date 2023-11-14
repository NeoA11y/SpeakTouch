/*
 * Global controller.
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

import com.neo.speaktouch.di.entrypoint.SpeechControllerEntryPoint
import com.neo.speaktouch.model.UiText
import com.neo.speaktouch.service.SpeakTouchService
import dagger.hilt.EntryPoints

object Controller {

    private val speakController
        get() = EntryPoints.get(
            checkNotNull(SpeakTouchService.context.get()),
            SpeechControllerEntryPoint::class.java
        ).getSpeechController()

    fun speak(text: UiText) {
        speakController.speak(text)
    }
}
