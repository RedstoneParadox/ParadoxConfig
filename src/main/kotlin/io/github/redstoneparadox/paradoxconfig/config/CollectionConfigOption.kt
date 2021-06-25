package io.github.redstoneparadox.paradoxconfig.config

import kotlin.reflect.KClass
import kotlin.reflect.full.cast
class CollectionConfigOption<E: Any, U: MutableCollection<E>>(private val innerType: KClass<E>, collectionType: KClass<U>, comment: String, key: String, value: U): ConfigOption<U>(collectionType, value, key, comment) {
    override fun set(any: Any?) {
        if (any is MutableCollection<*>) {
            for (element in any) {
                if (!innerType.isInstance(element)) {
                    return
                }
            }

            value.clear()
            value.addAll(type.cast(any))
        }
    }

    fun getElementKClass(): KClass<E> {
        return innerType
    }
}