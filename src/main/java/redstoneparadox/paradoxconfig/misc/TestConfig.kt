package redstoneparadox.paradoxconfig.misc

import redstoneparadox.paradoxconfig.config.AbstractConfig
import redstoneparadox.paradoxconfig.config.ConfigCategory
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import redstoneparadox.paradoxconfig.serialization.jankson.JanksonConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.jankson.JanksonConfigSerializer

object TestConfig: AbstractConfig("test.json5") {
    override val serializer: ConfigSerializer = JanksonConfigSerializer()
    override val deserializer: ConfigDeserializer = JanksonConfigDeserializer()

    val test: Boolean by option(true, "test")

    val testTwo: Long by rangedOption(2, 1..3L, "other", "")

    object InnerTestConfig: ConfigCategory("inner") {
        val testThree: Boolean by option(false, "third")
    }
}