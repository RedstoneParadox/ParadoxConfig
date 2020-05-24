package io.github.redstoneparadox.paradoxconfig.util

fun <T: Any> T?.unwrap(exception: Exception): T {
    if (this == null) throw exception
    return this
}

fun <E, L: MutableList<E>> L.toImmutable(): List<E> {
    return List(size) {this[it]}
}