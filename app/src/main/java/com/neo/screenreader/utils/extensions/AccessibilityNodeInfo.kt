package com.neo.screenreader.utils.extensions

import android.R
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

typealias NodeInfo = AccessibilityNodeInfoCompat
typealias NodeAction = AccessibilityNodeInfoCompat.AccessibilityActionCompat

val NodeInfo.isActionable: Boolean
    get() = isClickable || isLongClickable || isCheckable || isEditable

val NodeInfo.isReadable: Boolean
    get() = !contentDescription.isNullOrEmpty() ||
            !text.isNullOrEmpty() ||
            !hintText.isNullOrEmpty()

val NodeInfo.isButtonType: Boolean
    get() = listOf(
        "android.widget.Button",
        "android.widget.ImageButton",
    ).contains(className)

val NodeInfo.isAvailableForAccessibility: Boolean
    get() = isActionable || isReadable

fun NodeInfo.getLog() = buildList {
    add("class: ${className.ifEmptyOrNull { "unknown" }}")
    add("packageName: $packageName")
    add("isImportantForAccessibility: $isImportantForAccessibility")

    add("\nCONTENT")
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

    add("\nFOCUS")
    add("isFocusable: $isFocusable")
    add("isFocused: $isFocused")
    add("isAccessibilityFocused: $isAccessibilityFocused")

    add("\nREADE")
    add("isScreenReaderFocusable: $isScreenReaderFocusable")
    add("isAvailableForAccessibility: $isAvailableForAccessibility")
    add("isReadable: $isReadable")

    add("\nHIERARCHY")
    add("parent: ${parent?.className.ifEmptyOrNull { "unknown" }}")
    add("childCount: $childCount")

    add("\nACTION")
    add("isCheckable: $isCheckable")
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
        R.id.accessibilityActionScrollUp -> "ACTION_SCROLL_UP"
        R.id.accessibilityActionScrollLeft -> "ACTION_SCROLL_LEFT"
        R.id.accessibilityActionScrollDown -> "ACTION_SCROLL_DOWN"
        R.id.accessibilityActionScrollRight -> "ACTION_SCROLL_RIGHT"
        R.id.accessibilityActionPageDown -> "ACTION_PAGE_DOWN"
        R.id.accessibilityActionPageUp -> "ACTION_PAGE_UP"
        R.id.accessibilityActionPageLeft -> "ACTION_PAGE_LEFT"
        R.id.accessibilityActionPageRight -> "ACTION_PAGE_RIGHT"
        R.id.accessibilityActionShowOnScreen -> "ACTION_SHOW_ON_SCREEN"
        R.id.accessibilityActionScrollToPosition -> "ACTION_SCROLL_TO_POSITION"
        R.id.accessibilityActionContextClick -> "ACTION_CONTEXT_CLICK"
        R.id.accessibilityActionSetProgress -> "ACTION_SET_PROGRESS"
        R.id.accessibilityActionMoveWindow -> "ACTION_MOVE_WINDOW"
        R.id.accessibilityActionShowTooltip -> "ACTION_SHOW_TOOLTIP"
        R.id.accessibilityActionHideTooltip -> "ACTION_HIDE_TOOLTIP"
        R.id.accessibilityActionPressAndHold -> "ACTION_PRESS_AND_HOLD"
        R.id.accessibilityActionImeEnter -> "ACTION_IME_ENTER"
        R.id.accessibilityActionDragStart -> "ACTION_DRAG_START"
        R.id.accessibilityActionDragDrop -> "ACTION_DRAG_DROP"
        R.id.accessibilityActionDragCancel -> "ACTION_DRAG_CANCEL"
        else -> label.ifEmptyOrNull { "ACTION_UNKNOWN" }.toString()
    }