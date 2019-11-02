package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass

class CollectionConfigOption<T: Any, U: MutableCollection<T>>(private val innerType: KClass<T>, collectionType: KClass<U>, comment: String, key: String, value: U): ConfigOption<U>(collectionType, value, key, comment) {

    override fun deserialize(deserializer: ConfigDeserializer) {
        val newVal = deserializer.readOption(key)
        if (newVal is Collection<*>) {
            value.clear()

            for (thing in newVal) {
                if (thing != null && innerType.isInstance(thing)) {
                    (value as MutableCollection<T>).add(thing as T)
                }
            }
        }
    }
}