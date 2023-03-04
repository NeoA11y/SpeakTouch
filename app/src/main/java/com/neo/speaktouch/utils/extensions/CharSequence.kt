package com.neo.speaktouch.utils.extensions

fun <T : CharSequence> T?.ifEmptyOrNull(
    fallback : () -> T
) : T {
    return if (this.isNullOrEmpty()) {
        fallback()
    } else {
        this
    }
}

fun <T : List<CharSequence?>> T.filterNotNullOrEmpty() = filterNot { it.isNullOrEmpty() }