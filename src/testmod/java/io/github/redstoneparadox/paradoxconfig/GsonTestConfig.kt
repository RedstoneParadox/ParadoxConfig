package io.github.redstoneparadox.paradoxconfig

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import net.minecraft.util.Identifier

object GsonTestConfig: ConfigCategory("gson_test.json") {
    val boolean by option(true, "boolean", "A boolean value.")
    val double by option(2.5, "double", "A double value.")
    val string by option("Hi", "string", "A string value.")
    val rangedDouble by option(2.0, 1.0..4.0, "ranged_double", "A ranged double.")
    val identifier by option(Identifier("minecraft:empty"), "identifier")

    object Greetings: ConfigCategory("greetings") {
        val english by option(mutableListOf("Hi", "hey"), "english", "Greetings in English.")
        val mapping by option(mutableMapOf("Hi" to "Hola"), "mapping", "Maps English greetings to Spanish ones.")
    }
}