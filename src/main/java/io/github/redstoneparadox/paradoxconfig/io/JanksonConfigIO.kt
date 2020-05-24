package io.github.redstoneparadox.paradoxconfig.io

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import io.github.cottonmc.jankson.JanksonFactory
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.config.ConfigOption

class JanksonConfigIO: ConfigIO {
    private val jankson: Jankson = JanksonFactory.createJankson()

    override val fileExtension: String
        get() = "json5"

    override fun read(string: String, config: ConfigCategory) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(config: ConfigCategory): String {
        return serializeCategory(config).first.toJson(true, true)
    }

    fun serializeCategory(category: ConfigCategory): Pair<JsonObject, String> {
        val obj = JsonObject()

        for (subcategory in category.getSubcategories()) {
            val pair = serializeCategory(subcategory)
            obj.put(category.key, pair.first, pair.second)
        }

        for (option in category.getOptions()) {
            val pair = serializeOption(option)
            obj.put(option.key, pair.first, pair.second)
        }

        return Pair(obj, category.comment)
    }

    fun serializeOption(option: ConfigOption<*>): Pair<JsonElement, String> {
        val element = jankson.marshaller.serialize(option.get())
        return Pair(element, option.comment)
    }
}