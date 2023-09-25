/*
 * Representation of a text.
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

package com.neo.speaktouch.model

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {

    val args: List<Any>

    data class Raw(
        val text: String,
        override val args: List<Any>
    ) : UiText

    data class Res(
        @StringRes val res: Int,
        override val args: List<Any>
    ) : UiText

    fun resolved(context: Context): String {
        val text = when (this) {
            is Raw -> text
            is Res -> context.getString(res)
        }

        if (args.isEmpty()) {
            return text
        }

        val args = resolvedArgs(context)
            .toTypedArray()

        return String.format(text, *args)
    }

    fun resolvedArgs(context: Context): List<Any> {
        return args.map { arg ->
            when (arg) {
                is UiText -> {
                    arg.resolved(context)
                }

                else -> arg
            }
        }
    }

    companion object {
        operator fun invoke(
            text: String,
            vararg args: Any
        ) = Raw(text, args.toList())

        operator fun invoke(
            @StringRes res: Int,
            vararg args: Any
        ) = Res(res, args.toList())
    }
}