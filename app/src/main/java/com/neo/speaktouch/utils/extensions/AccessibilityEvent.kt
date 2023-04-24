/*
 * Extensions for AccessibilityEvent.
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

package com.neo.speaktouch.utils.extensions

import android.view.accessibility.AccessibilityEvent

fun AccessibilityEvent.getString(): String {
    return buildList {
        val eventName = when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "TYPE_VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> "TYPE_VIEW_LONG_CLICKED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> "TYPE_VIEW_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "TYPE_VIEW_TEXT_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> "TYPE_VIEW_HOVER_ENTER"
            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> "TYPE_VIEW_HOVER_EXIT"
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> "TYPE_NOTIFICATION_STATE_CHANGED"
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START -> "TYPE_TOUCH_EXPLORATION_GESTURE_START"
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> "TYPE_TOUCH_EXPLORATION_GESTURE_END"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "TYPE_WINDOW_CONTENT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> "TYPE_VIEW_TEXT_SELECTION_CHANGED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "TYPE_VIEW_SCROLLED"
            AccessibilityEvent.TYPE_ANNOUNCEMENT -> "TYPE_ANNOUNCEMENT"
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> "TYPE_VIEW_ACCESSIBILITY_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED"
            AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY"
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> "TYPE_GESTURE_DETECTION_START"
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> "TYPE_GESTURE_DETECTION_END"
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> "TYPE_TOUCH_INTERACTION_START"
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> "TYPE_TOUCH_INTERACTION_END"
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> "TYPE_WINDOWS_CHANGED"
            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED -> "TYPE_VIEW_CONTEXT_CLICKED"
            AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT -> "TYPE_ASSIST_READING_CONTEXT"
            AccessibilityEvent.TYPE_SPEECH_STATE_CHANGE -> "TYPE_SPEECH_STATE_CHANGE"
            else -> Integer.toHexString(eventType)
        }

        add("event: $eventName")
        add("source: ${source?.className}")
    }.joinToString(", ")
}
