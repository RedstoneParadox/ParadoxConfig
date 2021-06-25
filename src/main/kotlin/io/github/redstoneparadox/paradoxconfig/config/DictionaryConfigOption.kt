package io.github.redstoneparadox.paradoxconfig.config

import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DictionaryConfigOption<V: Any, T: MutableMap<String, V>>(private val valType: KClass<V>, mapType: KClass<T>, value: T, key: String, comment: String): ConfigOption<T>(mapType, value, key, comment) {
    override fun set(any: Any?) {
        if (any is MutableMap<*, *>) {
            for (element in any.keys) {
                if (element !is String) {
                    return
                }
            }

            for (element in any.values) {
                if (!valType.isInstance(element)) {
                    return
                }
            }

            value.clear()
            type.cast(any).forEach { value[it.key] = it.value }
        }
    }

    fun getValueKClass(): KClass<V> {
        return valType
    }
}