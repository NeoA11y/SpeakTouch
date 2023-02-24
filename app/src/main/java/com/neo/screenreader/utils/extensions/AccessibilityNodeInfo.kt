package com.neo.screenreader.utils.extensions

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

val AccessibilityNodeInfoCompat.isActionable: Boolean
    get() = isEnabled && (isClickable || isLongClickable)

val AccessibilityNodeInfoCompat.isReadable: Boolean
    get() = !text.isNullOrEmpty() ||
            !paneTitle.isNullOrEmpty() ||
            !hintText.isNullOrEmpty() ||
            !contentDescription.isNullOrEmpty()

val AccessibilityNodeInfoCompat.isButton: Boolean
    get() = className == "android.widget.Button"