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

package com.neo.speaktouch.utils.extension

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.neo.speaktouch.utils.`object`.NodeValidator
import com.neo.speaktouch.utils.`typealias`.NodeAction
import com.neo.speaktouch.utils.`typealias`.NodeInfo

fun NodeInfo.getNearestAncestor(
    predicate: (NodeInfo) -> Boolean
): NodeInfo? {
    var current: NodeInfo? = this.parent

    while (current != null && !predicate(current)) {
        current = current.parent
    }

    return current
}

fun NodeInfo.getString() = buildList {

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
    add("isSelected: $isSelected")
    add("stateDescription: $stateDescription")

    add("\nFOCUS")
    add("isFocusable: $isFocusable")
    add("isFocused: $isFocused")
    add("isAccessibilityFocused: $isAccessibilityFocused")
    add("isScreenReaderFocusable: $isScreenReaderFocusable")

    add("\nVALIDATOR")
    add("isAccessible: ${NodeValidator.isAccessible(this@getString)}")
    add("isReadable: ${NodeValidator.isReadable(this@getString)}")
    add("isRequestFocus: ${NodeValidator.isRequiredFocus(this@getString)}")
    add("isChildReadable: ${NodeValidator.isChildReadable(this@getString)}")

    add("\nHIERARCHY")
    add("parent: ${parent?.className.ifEmptyOrNull { "unknown" }}")
    add("childCount: $childCount")

    add("\nACTION")
    add("isCheckable: $isCheckable")
    add("isEditable: $isEditable")
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
