package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

class RangeConfigOption<T>(type: KClass<T>, value: T, key: String, comment: String, private val range: ClosedRange<T>): ConfigOption<T>(type, value, key, comment) where T : Any, T: Comparable<T> {

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (range.contains(value)) {
            this.value = value
        }
        else {
            throw Exception()
        }
    }

    override fun <E: Any> deserialize(deserializer: ConfigDeserializer<E>) {
        val any = deserializer.readValue(key)
        if (any != null) {
            val result = deserializer.tryDeserialize(any, type)
            if (result != null && range.contains(result)) {
                value = result
            }
        }
    }
}