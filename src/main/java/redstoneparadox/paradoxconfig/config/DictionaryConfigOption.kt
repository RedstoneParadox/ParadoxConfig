package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DictionaryConfigOption<V: Any, T: MutableMap<String, V>>(private val valType: KClass<V>, mapType: KClass<T>, value: T, key: String, comment: String): ConfigOption<T>(mapType, value, key, comment) {


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