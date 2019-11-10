package redstoneparadox.paradoxconfig.util

/**
 * Created by RedstoneParadox on 11/10/2019.
 */

operator fun Number.compareTo(other: Number): Int {
    if (this is Byte) return this.compareTo(other.toByte())
    if (this is Short) return this.compareTo(other.toShort())
    if (this is Int) return this.compareTo(other.toInt())
    if (this is Long) return this.compareTo(other.toLong())
    if (this is Float) return this.compareTo(other.toFloat())
    if (this is Double) return this.compareTo(other.toDouble())
    throw Exception("Something strange happened!")
}