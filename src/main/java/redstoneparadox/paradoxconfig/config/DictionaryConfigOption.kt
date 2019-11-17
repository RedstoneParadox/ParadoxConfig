package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.PConfigLogger
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DictionaryConfigOption<V: Any, T: MutableMap<String, V>>(private val valType: KClass<V>, mapType: KClass<T>, value: T, key: String, comment: String): ConfigOption<T>(mapType, value, key, comment) {

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
            PConfigLogger.error("Mismatch between expected and actual class of ${serializer::class}")
        }
    }

    override fun deserialize(deserializer: ConfigDeserializer) {
        val dictionary = deserializer.readDictionaryOption(key)

        if (dictionary != null) {
            value.clear()
            for((dKey, dVal) in dictionary.entries) {
                if (dKey is String && valType.isInstance(dVal)) {
                    value[dKey] = valType.cast(dVal)
                }
            }
        }
    }
}