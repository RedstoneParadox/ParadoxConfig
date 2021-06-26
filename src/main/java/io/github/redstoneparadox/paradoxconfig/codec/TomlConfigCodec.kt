package io.github.redstoneparadox.paradoxconfig.codec

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import net.minecraft.util.Identifier
import org.tomlj.Toml
import org.tomlj.TomlTable

class TomlConfigCodec: ConfigCodec {
    override val fileExtension: String = "toml"

    override fun decode(data: String, config: ConfigCategory) {
        val result = Toml.parse(data)
        if (!result.hasErrors()) {
            decodeCategory(config, result)
        }
    }

    private fun decodeCategory(category: ConfigCategory, table: TomlTable) {
        for (option in category.getOptions()) {
            val value = table[option.key]
            option.set(value)
        }

        for (subcategory in category.getSubcategories()) {
            val subcategoryTable = table[subcategory.key]

            if (subcategoryTable is TomlTable) decodeCategory(subcategory, subcategoryTable)
        }
    }

    override fun encode(config: ConfigCategory): String {
        val map: Map<String, Any> = encodeCategory(config)

        //return writer.write(map)
        TODO()
    }

    private fun encodeCategory(category: ConfigCategory): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()

        for (option in category.getOptions()) {
            val value = option.get()

            if (value is Identifier) {
                map[option.key] = value.toString()
            } else {
                map[option.key] = value
            }
        }

        for (subcategory in category.getSubcategories()) {
            val subcategoryMap: Map<String, Any> = encodeCategory(subcategory)
            map[subcategory.key] = subcategoryMap
        }

        return map
    }
}