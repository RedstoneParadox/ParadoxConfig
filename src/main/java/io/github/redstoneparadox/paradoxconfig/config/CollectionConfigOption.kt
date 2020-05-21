package io.github.redstoneparadox.paradoxconfig.config

import io.github.redstoneparadox.paradoxconfig.ParadoxConfig
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class CollectionConfigOption<T: Any, U: MutableCollection<T>>(private val innerType: KClass<T>, collectionType: KClass<U>, comment: String, key: String, value: U): ConfigOption<U>(collectionType, value, key, comment) {

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
            ParadoxConfig.error("Mismatch between expected and actual class of ${serializer::class}")
        }
    }

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
}