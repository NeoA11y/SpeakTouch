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

import android.view.accessibility.AccessibilityNodeInfo
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

operator fun NodeInfo.iterator() = object : Iterator<NodeInfo> {

    var index = -1

    override fun hasNext(): Boolean {
        return childCount != index + 1
    }

    override fun next(): NodeInfo {
        return getChild(++index)
    }

}

fun NodeInfo.getLog(vararg extra: String) = buildList {

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
    add("isValidForAccessible: ${NodeValidator.isValidForAccessibility(this@getLog)}")
    add("isReadable: ${NodeValidator.hasContentToRead(this@getLog)}")
    add("isRequestFocus: ${NodeValidator.mustFocus(this@getLog)}")
    add("isReadableAsChild: ${NodeValidator.isReadableAsChild(this@getLog)}")

    add("\nHIERARCHY")
    add("parent: ${parent?.className.ifEmptyOrNull { "unknown" }}")
    add("childCount: $childCount")

    add("\nACTION")
    add("isCheckable: $isCheckable")
    add("isEditable: $isEditable")
    add("isClickable: $isClickable")
    add("isLongClickable: $isLongClickable")
    add("actions: ${actionList.joinToString(", ") { it.name }}")

    if (extra.isNotEmpty()) {
        add("\nEXTRA")
        addAll(extra)
    }

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

fun AccessibilityNodeInfo.performFocus() {
    performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
}

context(ScanScope)
fun AccessibilityNodeInfo.performFocus(stop : Boolean = true) {
    this@ScanScope.stop = stop
    performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
}

fun AccessibilityNodeInfo.getFocusedOrNull(): AccessibilityNodeInfo? {
    return findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)
}

fun AccessibilityNodeInfo.getNextChildOrNull(
    target: AccessibilityNodeInfo? = null
): AccessibilityNodeInfo? {

    if (childCount == 0) return null

    if (target == null) return getChild(0)

    val currentIndex = indexOfChild(target)

    if (childCount > currentIndex + 1) return getChild(currentIndex + 1)

    return null
}

fun AccessibilityNodeInfo.getLastOrNull(): AccessibilityNodeInfo? {

    if (childCount == 0) return null

    return getChild(childCount - 1)
}

fun AccessibilityNodeInfo.getPreviousOrNull(
    target: AccessibilityNodeInfo
): AccessibilityNodeInfo? {

    val currentIndex = indexOfChild(target)

    if (currentIndex == 0) return null

    return getChild(currentIndex - 1)
}

fun AccessibilityNodeInfo.indexOfChild(
    target: AccessibilityNodeInfo
): Int {

    for (index in 0 until childCount) {

        val child = getChild(index)

        if (child == target) {
            return index
        }
    }

    error("Child not found")
}

val AccessibilityNodeInfo.lastIndex get() = childCount - 1