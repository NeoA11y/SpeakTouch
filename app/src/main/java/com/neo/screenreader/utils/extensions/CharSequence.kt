package com.neo.screenreader.utils.extensions

fun <T : CharSequence> T?.isEmptyOrNull(
    fallback : () -> T
) : T {
    return if (this.isNullOrEmpty()) {
        fallback()
    } else {
        this
    }
}