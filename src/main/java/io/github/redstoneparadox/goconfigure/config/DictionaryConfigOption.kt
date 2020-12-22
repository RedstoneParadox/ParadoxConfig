package io.github.redstoneparadox.goconfigure.config

import io.github.redstoneparadox.goconfigure.GoConfigure
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DictionaryConfigOption<V: Any, T: MutableMap<String, V>>(private val valType: KClass<V>, mapType: KClass<T>, value: T, key: String, comment: String): ConfigOption<T>(mapType, value, key, comment) {

    @Deprecated("Not used by new serialization system.")
    override fun <E: Any> serialize(serializer: ConfigSerializer<E>) {
        val out = serializer.createDictionary()
        for ((string, def) in value) {
            val serialized = serializer.trySerialize(def)
            if (serialized != null) {
                out[string] = serialized
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
        val dictionary = deserializer.readValue(key)
        if (dictionary is Map<*, *>) {
            val entries = dictionary.entries
            for ((dKey, dValue) in entries) {
                if (dKey is String && deserializer.eClass.isInstance(dValue)) {
                    val result = deserializer.tryDeserialize(deserializer.eClass.cast(dValue), valType)
                    if (result != null) {
                        value[dKey] = result
                    }
                }
            }
        }
    }

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