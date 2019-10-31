package redstoneparadox.paradoxconfig.misc

import redstoneparadox.paradoxconfig.config.ConfigCategory

fun runTests() {
    println("Is the test successful? ${TestConfig.test}")
    println("Value of the range option: ${TestConfig.testTwo}")
}

object TestConfig: ConfigCategory() {

    val test: Boolean by option(true, "test")

    val testTwo: Long by rangedOption(2L, 1..3L, "", "")
}