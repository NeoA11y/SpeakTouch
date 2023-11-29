/*
 * Extensions for CharSequence.
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

package com.neo.speaktouch.utils.extension

import timber.log.Timber

inline fun <T : CharSequence> T?.ifEmptyOrNull(
    fallback: () -> T
): T {
    return if (this.isNullOrEmpty()) {
        fallback()
    } else {
        this
    }
}

// TODO: Consider use a cache to avoid multiple calls to Class.forName
infix fun CharSequence.`is`(childClass: Class<*>): Boolean {

    if (equals(childClass.name)) return true
    if (isEmpty()) return false

    return runCatching {
        childClass.isAssignableFrom(Class.forName(toString()))
    }.onFailure {
        Timber.e(it)
    }.getOrElse {
        false
    }
}

fun CharSequence?.isNotNullOrEmpty(): Boolean {
    return !isNullOrEmpty()
}
