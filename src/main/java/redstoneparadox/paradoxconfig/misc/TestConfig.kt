package redstoneparadox.paradoxconfig.misc

import redstoneparadox.paradoxconfig.config.RootConfigCategory
import redstoneparadox.paradoxconfig.config.ConfigCategory
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import redstoneparadox.paradoxconfig.serialization.jankson.JanksonConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.jankson.JanksonConfigSerializer

object TestConfig: RootConfigCategory("test.json5") {
    override val serializer: ConfigSerializer = JanksonConfigSerializer()
    override val deserializer: ConfigDeserializer = JanksonConfigDeserializer()

    val test: Boolean by option(true, "test")

    val testTwo: Long by option(2L, 1..3L, "other", "")

    val collection: List<String> by option(arrayListOf("hi", "hey", "howdy", "hi"), "collection")

    object InnerTestConfig: ConfigCategory("inner") {
        val testThree: Boolean by option(false, "third")
    }
}