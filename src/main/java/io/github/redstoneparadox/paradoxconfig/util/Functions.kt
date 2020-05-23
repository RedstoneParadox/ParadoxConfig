package io.github.redstoneparadox.paradoxconfig.util

fun <T: Any> T?.unwrap(exception: Exception): T {
    if (this == null) throw exception
    return this
}