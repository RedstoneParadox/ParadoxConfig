package io.github.redstoneparadox.paradoxconfig.util

/**
 * Created by RedstoneParadox on 11/10/2019.
 */

internal operator fun Number.compareTo(other: Number): Int {
    return when (this) {
        is Byte -> this.compareTo(other.toDouble())
        is Short -> this.compareTo(other.toDouble())
        is Int -> this.compareTo(other.toDouble())
        is Long -> this.compareTo(other.toDouble())
        is Float -> this.compareTo(other.toDouble())
        is Double -> this.compareTo(other.toDouble())
        else -> throw Exception("Something strange happened!")
    }
}