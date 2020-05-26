package io.github.redstoneparadox.paradoxconfig.example

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory

@Suppress("unused")
object ExampleConfig: ConfigCategory("example.json5") {
    val boolean by option(true, "boolean", "A boolean value.")
    val double by option(2.5, "double", "A double value.")
    val string by option("Hi", "string", "A string value.")
    val rangedDouble by option(2.0, 1.0..4.0, "ranged_double", "A ranged double.")

    object Greetings: ConfigCategory("greetings") {
        val english by option(mutableListOf("Hi", "hey"), "english", "Greetings in English.")
        val mapping by option(mutableMapOf("Hi" to "Hola"), "mapping", "Maps English greetings to Spanish ones.")
    }
}