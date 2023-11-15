/*
 * Service Wrapper.
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

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
import com.neo.speaktouch.utils.extension.getFocusedOrNull
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class ServiceController @Inject constructor(
    private val service: AccessibilityService
) {

    fun getOrThrow(): AccessibilityService {
        return service
    }

    fun getFocused(): AccessibilityNodeInfo? {
        return getRoot().getFocusedOrNull()
    }

    fun getRoot(): AccessibilityNodeInfo {
        return getOrThrow().rootInActiveWindow
    }

    fun performGlobalAction(action: Int): Boolean {
        return getOrThrow().performGlobalAction(action)
    }
}
