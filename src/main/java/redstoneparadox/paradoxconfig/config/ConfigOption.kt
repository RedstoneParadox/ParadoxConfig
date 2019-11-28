package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

open class ConfigOption<T: Any>(val type: KClass<T>, var value: T, val key: String, val comment: String, private var temp: T? = null) {

    open operator fun getValue(thisRef : Any?, property: KProperty<*>): T {
        return value
    }

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    internal open fun set(any: Any?) {
        if (type.isInstance(any)) {
            value = type.cast(any)
        }
    }

    // Only to be used by config screens.
    fun setTemp(any: Any) {
        if (type.isInstance(any)) {
            temp = type.cast(any)
        }
        else {
            temp == null
        }
    }

    // Only to be used by config screen builders
    fun getCurrent(): T {
        if (temp != null) {
            return temp as T
        }
        return value
    }

    fun save() {
        if (temp != null) value = temp as T
        temp = null
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