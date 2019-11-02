package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import kotlin.reflect.KClass

class ArrayConfigOption<T: Any>(private val innerType: KClass<T>, comment: String, key: String, value: MutableCollection<T>): ConfigOption<MutableCollection<*>>(MutableCollection::class, value, key, comment) {

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