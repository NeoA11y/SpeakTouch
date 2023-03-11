/*
 * Extensions for NodeInfo (AccessibilityNodeInfoCompat).
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

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

typealias NodeInfo = AccessibilityNodeInfoCompat
typealias NodeAction = AccessibilityNodeInfoCompat.AccessibilityActionCompat

val NodeInfo.hasAnyClick: Boolean
    get() = isClickable || isLongClickable

val NodeInfo.isActionable: Boolean
    get() = hasAnyClick || isFocusable

val NodeInfo.isReadable: Boolean
    get() = !isIgnore && (hasText || hasStateDescription)

val NodeInfo.hasText: Boolean
    get() = !contentDescription.isNullOrEmpty() ||
            !text.isNullOrEmpty() ||
            !hintText.isNullOrEmpty()

val NodeInfo.hasStateDescription: Boolean
    get() = !stateDescription.isNullOrEmpty() || isCheckable

val NodeInfo.isAvailableForAccessibility: Boolean
    get() = !isIgnore && (isRequiredFocus || isReadable)

val NodeInfo.isRequiredFocus
    get() = !isIgnore && (isActionable || isScreenReaderFocusable)

val NodeInfo.isIgnore
    get() = !isImportantForAccessibility || !isVisibleToUser

fun NodeInfo.getNearestAncestor(
    predicate: (NodeInfo) -> Boolean
): NodeInfo? {
    var current: NodeInfo? = this.parent

    while (current != null && !predicate(current)) {
        current = current.parent
    }

    return current
}

fun NodeInfo.getLog() = buildList {
    add("class: $className")
    add("packageName: $packageName")
    add("isImportantForAccessibility: $isImportantForAccessibility")

    add("\nCONTENT")
    add("id: $viewIdResourceName") // TODO: enable flagReportViewIds
    add("contentDescription: $contentDescription")
    add("text: $text")
    add("hintText: $hintText")
    add("isHeading: $isHeading")
    add("isPassword: $isPassword")
    add("error: $error")

    add("\nSTATE")
    add("isScrollable: $isScrollable")
    add("isEnabled: $isEnabled")
    add("isChecked: $isChecked")
    add("stateDescription: $stateDescription")

    add("\nFOCUS")
    add("isFocusable: $isFocusable")
    add("isFocused: $isFocused")
    add("isAccessibilityFocused: $isAccessibilityFocused")

    add("\nREADE")
    add("isScreenReaderFocusable: $isScreenReaderFocusable")
    add("isAvailableForAccessibility: $isAvailableForAccessibility")
    add("isReadable: $isReadable")
    add("isVisibleToUser: $isVisibleToUser")

    add("\nHIERARCHY")
    add("parent: ${parent?.className.ifEmptyOrNull { "unknown" }}")
    add("childCount: $childCount")

    add("\nACTION")
    add("isCheckable: $isCheckable")
    add("isEditable: $isEditable")
    add("isActionable: $isActionable")
    add("isClickable: $isClickable")
    add("isLongClickable: $isLongClickable")
    add("actions: ${actionList.joinToString(", ") { it.name }}")

}.joinToString("\n")

private val NodeAction.name: String
    get() = when (id) {
        AccessibilityNodeInfoCompat.ACTION_FOCUS -> "ACTION_FOCUS"
        AccessibilityNodeInfoCompat.ACTION_CLEAR_FOCUS -> "ACTION_CLEAR_FOCUS"
        AccessibilityNodeInfoCompat.ACTION_SELECT -> "ACTION_SELECT"
        AccessibilityNodeInfoCompat.ACTION_CLEAR_SELECTION -> "ACTION_CLEAR_SELECTION"
        AccessibilityNodeInfoCompat.ACTION_CLICK -> "ACTION_CLICK"
        AccessibilityNodeInfoCompat.ACTION_LONG_CLICK -> "ACTION_LONG_CLICK"
        AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS -> "ACTION_ACCESSIBILITY_FOCUS"
        AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS -> "ACTION_CLEAR_ACCESSIBILITY_FOCUS"
        AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY -> "ACTION_NEXT_AT_MOVEMENT_GRANULARITY"
        AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY -> "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY"
        AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT -> "ACTION_NEXT_HTML_ELEMENT"
        AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT -> "ACTION_PREVIOUS_HTML_ELEMENT"
        AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD -> "ACTION_SCROLL_FORWARD"
        AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD -> "ACTION_SCROLL_BACKWARD"
        AccessibilityNodeInfoCompat.ACTION_CUT -> "ACTION_CUT"
        AccessibilityNodeInfoCompat.ACTION_COPY -> "ACTION_COPY"
        AccessibilityNodeInfoCompat.ACTION_PASTE -> "ACTION_PASTE"
        AccessibilityNodeInfoCompat.ACTION_SET_SELECTION -> "ACTION_SET_SELECTION"
        AccessibilityNodeInfoCompat.ACTION_EXPAND -> "ACTION_EXPAND"
        AccessibilityNodeInfoCompat.ACTION_COLLAPSE -> "ACTION_COLLAPSE"
        AccessibilityNodeInfoCompat.ACTION_SET_TEXT -> "ACTION_SET_TEXT"
        else -> label.ifEmptyOrNull { "ACTION_UNKNOWN" }.toString()
    }