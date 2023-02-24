package com.neo.screenreader.utils.extensions

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

val AccessibilityNodeInfoCompat.isActionable: Boolean
    get() = isClickable || isLongClickable || isClickable

val AccessibilityNodeInfoCompat.isReadable: Boolean
    get() = !text.isNullOrEmpty() ||
            !paneTitle.isNullOrEmpty() ||
            !hintText.isNullOrEmpty() ||
            !contentDescription.isNullOrEmpty() ||
            !stateDescription.isNullOrEmpty() ||
            !error.isNullOrEmpty()