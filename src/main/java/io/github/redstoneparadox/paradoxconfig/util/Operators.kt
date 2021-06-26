package io.github.redstoneparadox.paradoxconfig.util

/**
 * Created by RedstoneParadox on 11/10/2019.
 */

internal operator fun Number.compareTo(other: Number): Int {
    return when (this) {
        is Byte -> this.compareTo(other.toLong())
        is Short -> this.compareTo(other.toLong())
        is Int -> this.compareTo(other.toLong())
        is Long -> this.compareTo(other.toLong())
        is Float -> this.compareTo(other.toFloat())
        is Double -> this.compareTo(other.toDouble())
        else -> throw Exception("Something strange happened!")
    }
}