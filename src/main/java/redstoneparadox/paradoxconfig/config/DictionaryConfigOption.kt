package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DictionaryConfigOption<K: Any, V: Any, T: MutableMap<K, V>>(private val keyType: KClass<K>, private val valType: KClass<V>, mapType: KClass<T>, value: T, key: String, comment: String): ConfigOption<T>(mapType, value, key, comment) {


    override fun deserialize(deserializer: ConfigDeserializer) {
        val dictionary = deserializer.readDictionaryOption(key)

        if (dictionary != null) {
            value.clear()
            for((dKey, dVal) in dictionary.entries) {
                if (keyType.isInstance(dKey) && valType.isInstance(dVal)) {
                    value[keyType.cast(dKey)] = valType.cast(dVal)
                }
            }
        }
    }
}