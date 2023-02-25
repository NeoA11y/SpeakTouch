package com.neo.screenreader.utils.extensions

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

val AccessibilityNodeInfoCompat.isActionable: Boolean
    get() = isClickable || isLongClickable || isClickable

val AccessibilityNodeInfoCompat.isReadable: Boolean
    get() = !contentDescription.isNullOrEmpty() ||
                !text.isNullOrEmpty() ||
                !hintText.isNullOrEmpty()

val AccessibilityNodeInfoCompat.isButtonType: Boolean
    get() = className == "android.widget.Button"

fun AccessibilityNodeInfoCompat.getLog(): String {
    return String.format(
        "class: %s\n" +
                "isFocusable: %s\n" +
                "isEnabled: %s\n" +
                "isClickable: %s\n" +
                "isLongClickable: %s\n" +
                "text: %s\n" +
                "hint: %s\n" +
                "contentDescription: %s\n" +
                "stateDescription: %s\n" +
                "error: %s\n" +
                "isHeading: %s\n" +
                "paneTitle: %s\n" +
                "isScreenReaderFocusable: %s\n" +
                "isImportantForAccessibility: %s\n" +
                "parent: %s\n",
        className.ifEmpty { "unknown" },
        isEnabled,
        isFocusable,
        isClickable,
        isLongClickable,
        text,
        hintText,
        contentDescription,
        stateDescription,
        error,
        isHeading,
        paneTitle,
        isScreenReaderFocusable,
        isImportantForAccessibility,
        parent?.className?.ifEmpty { "unknown" },
    )
}