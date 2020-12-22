package io.github.redstoneparadox.paradoxconfig.config

import io.github.goconfigure.paradoxconfig.serialization.ConfigDeserializer
import io.github.goconfigure.paradoxconfig.serialization.ConfigSerializer
import io.github.redstoneparadox.goconfigure.config.ConfigCategory

/**
 * Inheritors of this class represent the root config category in a
 * config file. For inner categories, please extend [ConfigCategory]
 * instead.
 *
 * @param file The name of the config file in the fabric config folder.
 * @property serializer The [ConfigSerializer] to use when serializing
 *                      the config to a file.
 * @property deserializer The [ConfigDeserializer] to use when
 *                        deserializing the config from a file.
 */
@Deprecated("Not used by new serialization system.", ReplaceWith("ConfigCategory"))
abstract class RootConfigCategory(val file: String): ConfigCategory() {
    abstract val serializer: ConfigSerializer<*>
    abstract val deserializer: ConfigDeserializer<*>
}