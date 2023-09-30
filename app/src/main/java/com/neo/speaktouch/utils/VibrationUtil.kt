/*
 * Vibration utility wrapper.
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
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibrationUtil(private val vibrator: Vibrator) {

    @Suppress("deprecation")
    constructor(context: Context) : this(
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    )

    fun vibrateEffectHeavyClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(
                VibrationEffect.createPredefined(
                    VibrationEffect.EFFECT_HEAVY_CLICK
                )
            )
        } else {
            vibrateLegacy(
                LegacyVibrationEffect.HeavyClick
            )
        }
    }

    fun vibrateEffectTick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(
                VibrationEffect.createPredefined(
                    VibrationEffect.EFFECT_TICK
                )
            )
        } else {
            vibrateLegacy(
                LegacyVibrationEffect.Tick,
            )
        }
    }

    @Suppress("deprecation")
    fun vibrateLegacy(
        legacyVibrationEffect: LegacyVibrationEffect,
        repeat: Int = DEFAULT_REPEAT,
    ) {
        vibrator.vibrate(
            legacyVibrationEffect.pattern,
            repeat
        )
    }

    fun hasVibrator() = vibrator.hasVibrator()

    fun finish() = vibrator.cancel()

    companion object {
        private const val DEFAULT_REPEAT = -1
    }
}

