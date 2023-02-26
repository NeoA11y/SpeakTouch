package com.neo.screenreader.utils.extensions

inline fun <reified T> Iterable<Any>.getInstance(): T {
    return first { it is T } as T
}
