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

infix fun CharSequence.instanceOf(childClass: Class<*>): Boolean {
    if (equals(childClass.name)) return true

    val superClazz = Class.forName(toString())

    return childClass.isAssignableFrom(superClazz)
}
