package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer

abstract class AbstractConfig(val file: String): ConfigCategory() {
    abstract val serializer: ConfigSerializer
    abstract val deserializer: ConfigDeserializer
}