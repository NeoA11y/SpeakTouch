package com.neo.screenreader.utils.extensions

fun <T : CharSequence> T?.ifEmptyOrNull(
    fallback : () -> T
) : T {
    return if (this.isNullOrEmpty()) {
        fallback()
    } else {
        this
    }
}