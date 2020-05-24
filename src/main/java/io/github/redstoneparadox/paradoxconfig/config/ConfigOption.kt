package io.github.redstoneparadox.paradoxconfig.config

import io.github.redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

open class ConfigOption<T: Any>(protected val type: KClass<T>, protected var value: T, internal val key: String, val comment: String) {

    open operator fun getValue(thisRef : Any?, property: KProperty<*>): T {
        return value
    }

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    open fun set(any: Any?) {
        if (type.isInstance(any)) {
            value = type.cast(any)
        }
    }

    open fun get(): Any {
        return value
    }

    fun getKClass(): KClass<*> {
        return type
    }

    internal open fun <E: Any> serialize(serializer: ConfigSerializer<E>) {
        val serialized = serializer.trySerialize(value)
        if (serialized != null) {
            serializer.writeValue(key, serialized, comment)
        }
    }

    internal open fun <E: Any> deserialize(deserializer: ConfigDeserializer<E>) {
        val any = deserializer.readValue(key)
        if (any != null) {
            val result = deserializer.tryDeserialize(any, type)
            if (result != null) {
                value = result
            }
        }
    }
}