package io.github.redstoneparadox.paradoxconfig.codec

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import net.minecraft.util.Identifier


class TomlConfigCodec: ConfigCodec {
    override val fileExtension: String = "toml"
    val writer: TomlWriter = TomlWriter()

    override fun decode(data: String, config: ConfigCategory) {
        val map = Toml().read(data).toMap()

        decodeCategory(config, map)
    }

    private fun decodeCategory(category: ConfigCategory, map: Map<String, Any>) {
        for (option in category.getOptions()) {
            val value = map[option.key]
            option.set(value)
        }

        for (subcategory in category.getSubcategories()) {
            val subcategoryTable = map[subcategory.key]

            if (subcategoryTable is Map<*, *>) decodeCategory(subcategory, subcategoryTable as Map<String, Any>)
        }
    }

    override fun encode(config: ConfigCategory): String {
        val map: Map<String, Any> = encodeCategory(config)

        return writer.write(map)
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