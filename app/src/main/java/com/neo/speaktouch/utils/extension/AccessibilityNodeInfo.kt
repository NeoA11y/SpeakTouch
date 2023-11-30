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
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat
import com.neo.speaktouch.R
import com.neo.speaktouch.model.Type
import com.neo.speaktouch.model.Text
import com.neo.speaktouch.utils.NodeValidator

fun AccessibilityNodeInfoCompat.getNearestAncestor(
    predicate: (AccessibilityNodeInfoCompat) -> Boolean
): AccessibilityNodeInfoCompat? {
    var current: AccessibilityNodeInfoCompat? = this.parent

    while (current != null && !predicate(current)) {
        current = current.parent
    }

    return current
}

operator fun AccessibilityNodeInfoCompat.iterator() =
    object : Iterator<AccessibilityNodeInfoCompat> {

        var index = -1

        override fun hasNext(): Boolean {
            return childCount != index + 1
        }

        override fun next(): AccessibilityNodeInfoCompat {
            return getChild(++index)
        }

    }

fun AccessibilityNodeInfoCompat.getLog(vararg extra: String) = buildList {

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

private val AccessibilityActionCompat.name: String
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

context(NodeScanScope)
fun AccessibilityNodeInfo.performFocus(mustStop: Boolean = true) {

    performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)

    if (mustStop) stop(result = this)
}

fun AccessibilityNodeInfo.getFocusedOrNull(): AccessibilityNodeInfo? {
    return findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)
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

fun AccessibilityNodeInfoCompat.toStateText(
    type: Type? = Type.get(this)
): Text? {

    if (type is Type.Checkable) {
        return toCheckableStateText(type)
    }

    // The selection state has a deliberately different behavior from talkback.
    // Discussion at: https://github.com/NeoA11y/SpeakTouch/discussions/115

    if (isSelected && stateDescription.isNotNullOrEmpty()) {
        return Text(stateDescription.toString())
    }

    if (isSelected) {
        return Text(R.string.text_selected)
    }

    return null
}

fun AccessibilityNodeInfoCompat.toCheckableStateText(
    type: Type.Checkable
): Text? {
    when (type) {
        Type.Checkable.Checkbox -> {
            if (stateDescription.isNullOrEmpty()) {
                return if (isChecked) {
                    Text(R.string.text_checked)
                } else {
                    Text(R.string.text_not_checked)
                }
            }

            return Text(stateDescription.toString())
        }

        Type.Checkable.Radio -> {
            if (stateDescription.isNullOrEmpty()) {
                return if (isChecked) {
                    Text(R.string.text_selected)
                } else {
                    Text(R.string.text_not_selected)
                }
            }

            return Text(stateDescription.toString())
        }

        Type.Checkable.Switch -> {
            if (stateDescription.isNullOrEmpty()) {
                return if (isChecked) {
                    Text(R.string.text_enabled)
                } else {
                    Text(R.string.text_disabled)
                }
            }

            return Text(stateDescription.toString())
        }

        Type.Checkable.Toggle -> {
            if (stateDescription.isNullOrEmpty()) {
                return if (isChecked) {
                    Text(R.string.text_pressed)
                } else {
                    Text(R.string.text_not_pressed)
                }
            }

            return Text(stateDescription.toString())
        }

        Type.Checkable.TextView -> {

            if (!isChecked) return null /* don't speak */

            if (stateDescription.isNullOrEmpty()) {
                return Text(R.string.text_selected)
            }

            return Text(stateDescription.toString())
        }

        Type.Checkable.Custom -> {
            if (stateDescription.isNullOrEmpty()) {
                return if (isChecked) {
                    Text(R.string.text_selected)
                } else {
                    Text(R.string.text_not_selected)
                }
            }

            return Text(stateDescription.toString())
        }
    }
}


fun AccessibilityNodeInfoCompat.getContent(
    type: Type? = Type.get(this)
): CharSequence? {

    // Deliberately different behavior from talkback
    // https://github.com/NeoA11y/SpeakTouch/discussions/119
    // https://github.com/NeoA11y/SpeakTouch/discussions/121

    if (type is Type.EditField) {

        return when {
            text.isNotNullOrEmpty() -> text
            hintText.isNotNullOrEmpty() -> hintText
            else -> null
        }
    }

    if (contentDescription.isNotNullOrEmpty()) {
        return contentDescription
    }

    if (text.isNotNullOrEmpty()) {
        return text
    }

    return null
}
