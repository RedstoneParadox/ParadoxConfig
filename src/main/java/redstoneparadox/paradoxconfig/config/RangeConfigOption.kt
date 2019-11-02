package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class RangeConfigOption<T>(type: KClass<T>, value: T, key: String, comment: String, private val range: ClosedRange<T>): ConfigOption<T>(type, value, key, comment) where T : Number, T: Comparable<T> {

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (range.contains(value)) {
            this.value = value
        }
        else {
            throw Exception()
        }
    }

    override fun deserialize(deserializer: ConfigDeserializer) {
        val newVal = deserializer.readOption(key)
        if (newVal != null && type.isInstance(newVal)) {
            if (range.contains(newVal as T)) value = newVal
        }
    }
}