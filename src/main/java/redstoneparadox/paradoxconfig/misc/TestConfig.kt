package redstoneparadox.paradoxconfig.misc

import redstoneparadox.paradoxconfig.config.AbstractConfig
import redstoneparadox.paradoxconfig.config.ConfigCategory

object TestConfig: AbstractConfig("test.json") {

    val test: Boolean by option(true, "test")

    val testTwo: Long by rangedOption(2L, 1..3L, "other", "")

    object InnerTestConfig: ConfigCategory("inner") {
        val testThree: Boolean by option(false, "third")
    }
}