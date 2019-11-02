package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass

class CollectionConfigOption<T: Any, U: MutableCollection<T>>(private val innerType: KClass<T>, collectionType: KClass<U>, comment: String, key: String, value: U): ConfigOption<U>(collectionType, value, key, comment) {

    override fun deserialize(deserializer: ConfigDeserializer) {
        val collection = deserializer.readCollectionOption(key)
        if (collection != null) {
            value.clear()
            for (thing in collection) {
                if (innerType.isInstance(thing)) {
                    value.add(thing as T)
                }
            }
        }
    }
}