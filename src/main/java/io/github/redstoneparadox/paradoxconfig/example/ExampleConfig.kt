package io.github.redstoneparadox.paradoxconfig.example

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory

@Suppress("unused")
object ExampleConfig: ConfigCategory("example.json5") {
    val boolean by option(true, "boolean", "A boolean value.")
    val double by option(2.5, "double", "A double value.")
}