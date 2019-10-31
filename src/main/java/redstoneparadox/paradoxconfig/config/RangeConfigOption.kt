package redstoneparadox.paradoxconfig.config

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
}