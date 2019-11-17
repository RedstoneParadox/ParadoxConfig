package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.PConfigLogger
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
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
            PConfigLogger.error("Mismatch between expected and actual class of ${serializer::class}")
        }
    }

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