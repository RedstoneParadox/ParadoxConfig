package io.github.redstoneparadox.paradoxconfig.util

import com.google.gson.JsonPrimitive
import io.github.redstoneparadox.paradoxconfig.ParadoxConfig
import java.lang.reflect.Field

fun <T: Any> T?.unwrap(exception: Exception): T {
    if (this == null) throw exception
    return this
}

fun <E, L: MutableList<E>> L.toImmutable(): List<E> {
    return List(size) {this[it]}
}

fun <T> T?.ifNull(t: T): T? {
    if (this == null) return t
    return this
}

fun JsonPrimitive.getValue(): Any? {
    return try {
        val valueField: Field = this.javaClass.getDeclaredField("value")

        valueField.isAccessible = true
        valueField[this]
    } catch (e: Exception) {
        ParadoxConfig.error("Encountered exception when attempting to unwrap GSON primitive!")
        e.printStackTrace()

        null
    }
}

val JsonPrimitive.asAny: Any?
    get() {
        return try {
            val valueField: Field = this.javaClass.getDeclaredField("value")

            valueField.isAccessible = true
            valueField[this]
        } catch (e: Exception) {
            ParadoxConfig.error("Encountered exception when attempting to unwrap GSON primitive!")
            e.printStackTrace()

            null
        }
    }