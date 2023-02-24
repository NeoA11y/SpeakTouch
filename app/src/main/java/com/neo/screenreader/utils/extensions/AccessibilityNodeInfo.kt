package com.neo.screenreader.utils.extensions

import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

val AccessibilityNodeInfo.isAccessibilityFocusable: Boolean
    get() = AccessibilityNodeInfoCompat.wrap(this).isAccessibilityFocusable

val AccessibilityNodeInfoCompat.isAccessibilityFocusable: Boolean
    get() = isClickableOrFocusable ||
            isScreenReaderFocusable ||
            hasText && isImportantForAccessibility

val AccessibilityNodeInfoCompat.isClickableOrFocusable: Boolean
    get() = isFocusable ||
            isClickable ||
            isLongClickable

val AccessibilityNodeInfoCompat.hasText: Boolean
    get() = !text.isNullOrEmpty() ||
            !hintText.isNullOrEmpty() ||
            !contentDescription.isNullOrEmpty()