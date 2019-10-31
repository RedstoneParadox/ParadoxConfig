package redstoneparadox.paradoxconfig.config

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

open class ConfigOption<T: Any>(val type: KClass<T>, var value: T, val key: String, val comment: String) {

    open operator fun getValue(thisRef : Any?, property: KProperty<*>): T {
        return value
    }

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}