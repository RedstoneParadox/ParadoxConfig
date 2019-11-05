package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DictionaryConfigOption<K: Any, V: Any, T: MutableMap<K, V>>(private val keyType: KClass<K>, private val defType: KClass<V>, mapType: KClass<T>, value: T, key: String, comment: String): ConfigOption<T>(mapType, value, key, comment) {


    override fun deserialize(deserializer: ConfigDeserializer) {
        val dictionary = deserializer.readDictionaryOption(key)

        if (dictionary != null) {
            value.clear()
            for((dkey, dvalue) in dictionary.entries) {
                if (keyType.isInstance(dkey) && defType.isInstance(dvalue)) {
                    value[keyType.cast(dkey)] = defType.cast(dvalue)
                }
            }
        }
    }
}