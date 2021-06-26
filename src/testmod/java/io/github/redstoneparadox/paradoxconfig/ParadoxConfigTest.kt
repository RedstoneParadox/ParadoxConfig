package io.github.redstoneparadox.paradoxconfig

import net.fabricmc.api.ModInitializer

object ParadoxConfigTest: ModInitializer {
    override fun onInitialize() {
        println()

        println("Jankson Test Config:")
        println("boolean: ${JanksonTestConfig.boolean}")
        println("double: ${JanksonTestConfig.double}")
        println("string: ${JanksonTestConfig.string}")
        println("rangedDouble: ${JanksonTestConfig.rangedDouble}")
        println("identifier: ${JanksonTestConfig.identifier}")
        println("greetings.english: ${JanksonTestConfig.Greetings.english}")
        println("greetings.mapping: ${JanksonTestConfig.Greetings.mapping}")
        println()

        println("Gson Test Config:")
        println("boolean: ${GsonTestConfig.boolean}")
        println("double: ${GsonTestConfig.double}")
        println("string: ${GsonTestConfig.string}")
        println("rangedDouble: ${GsonTestConfig.rangedDouble}")
        println("identifier: ${GsonTestConfig.identifier}")
        println("greetings.english: ${GsonTestConfig.Greetings.english}")
        println("greetings.mapping: ${GsonTestConfig.Greetings.mapping}")
        println()

        println("Toml Test Config:")
        println("boolean: ${TomlTestConfig.boolean}")
        println("double: ${TomlTestConfig.double}")
        println("string: ${TomlTestConfig.string}")
        println("rangedDouble: ${TomlTestConfig.rangedDouble}")
        println("identifier: ${TomlTestConfig.identifier}")
        println("greetings.english: ${TomlTestConfig.Greetings.english}")
        println("greetings.mapping: ${TomlTestConfig.Greetings.mapping}")
        println("greetings.subsubcategory.foo: ${TomlTestConfig.Greetings.SubSubCategory.foo}")
        println()
    }
}