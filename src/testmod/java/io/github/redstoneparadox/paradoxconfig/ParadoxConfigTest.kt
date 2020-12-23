package io.github.redstoneparadox.paradoxconfig

import io.github.redstoneparadox.paradoxconfig.ExampleConfig
import net.fabricmc.api.ModInitializer

object ParadoxConfigTest: ModInitializer {
    override fun onInitialize() {
        println("Example config option values:")
        println("boolean: ${ExampleConfig.boolean}")
        println("double: ${ExampleConfig.double}")
        println("string: ${ExampleConfig.string}")
        println("rangedDouble: ${ExampleConfig.rangedDouble}")
        println("identifier: ${ExampleConfig.identifier}")
        println("greetings.english: ${ExampleConfig.Greetings.english}")
        println("greetings.mapping: ${ExampleConfig.Greetings.mapping}")
    }
}