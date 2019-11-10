package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

open class ConfigOption<T: Any>(val type: KClass<T>, var value: T, val key: String, val comment: String) {

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

    internal open fun serialize(serializer: ConfigSerializer) {
        serializer.writeOption(key, value, comment)
    }

    internal open fun deserialize(deserializer: ConfigDeserializer) {
        val any = deserializer.readOption(key)
        if (any != null && type.isInstance(any)) {
            value = type.cast(any)
        }
    }
}