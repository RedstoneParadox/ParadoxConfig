package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class CollectionConfigOption<T: Any, U: MutableCollection<T>>(private val innerType: KClass<T>, collectionType: KClass<U>, comment: String, key: String, value: U): ConfigOption<U>(collectionType, value, key, comment) {

    override fun deserialize(deserializer: ConfigDeserializer) {
        val collection = deserializer.readCollectionOption(key)
        if (collection != null) {
            value.clear()
            for (any in collection) {
                if (innerType.isInstance(any)) {
                    value.add(innerType.cast(any))
                }
            }
        }
    }
}