package io.github.goconfigure.config

import io.github.goconfigure.GoConfigure
import io.github.goconfigure.serialization.ConfigDeserializer
import io.github.goconfigure.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class CollectionConfigOption<E: Any, U: MutableCollection<E>>(private val innerType: KClass<E>, collectionType: KClass<U>, comment: String, key: String, value: U): ConfigOption<U>(collectionType, value, key, comment) {

    @Deprecated("Not used by new serialization system.")
    override fun <E: Any> serialize(serializer: ConfigSerializer<E>) {
        val out = serializer.createCollection()
        for (entry in value) {
            val serialized = serializer.trySerialize(entry)
            if (serialized != null) {
                out.add(serialized)
            }
        }
        if (serializer.eClass.isInstance(out)) {
            serializer.writeValue(key, serializer.eClass.cast(out), comment)
        }
        else {
            GoConfigure.error("Mismatch between expected and actual class of ${serializer::class}")
        }
    }

    @Deprecated("Not used by new serialization system.")
    override fun <E: Any> deserialize(deserializer: ConfigDeserializer<E>) {
        val collection = deserializer.readValue(key)
        if (collection is Collection<*>) {
            val iterator = collection.iterator()
            value.clear()
            for (any in iterator) {
                if (deserializer.eClass.isInstance(any)) {
                    val result = deserializer.tryDeserialize(deserializer.eClass.cast(any), innerType)
                    if (result != null) {
                        value.add(result)
                    }
                }
            }
        }
    }

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