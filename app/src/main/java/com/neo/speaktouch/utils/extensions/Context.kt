package com.neo.speaktouch.utils.extensions

import android.content.Context
import com.neo.speaktouch.R

fun Context.getText(isEnabled: Boolean): String {
    return if (isEnabled) {
        getString(R.string.text_enabled)
    } else {
        getString(R.string.text_disabled)
    }
}
