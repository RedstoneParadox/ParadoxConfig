package io.github.redstoneparadox.paradoxconfig.io

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import io.github.cottonmc.jankson.JanksonFactory
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.config.ConfigOption

class JanksonConfigIO: ConfigIO {
    private val jankson: Jankson = JanksonFactory.createJankson()

    override val fileExtension: String
        get() = "json5"

    override fun read(string: String, config: ConfigCategory) {
        val json = jankson.load(string)
        deserializeCategory(json, config)
    }

    private fun deserializeCategory(json: JsonObject, category: ConfigCategory) {
        for (subcategory in category.getSubcategories()) {
            val obj = json[subcategory.key]
            if (obj is JsonObject) deserializeCategory(obj, subcategory)
        }

        for (option in category.getOptions()) {
            val element = json[option.key]
            if (element != null) deserializeOption(element, option)
        }
    }

    private fun deserializeOption(json: JsonElement, option: ConfigOption<*>) {
        val any = jankson.marshaller.marshall(option.getKClass().java, json)
        option.set(any)
    }

    override fun write(config: ConfigCategory): String {
        return serializeCategory(config).first.toJson(true, true)
    }

    private fun serializeCategory(category: ConfigCategory): Pair<JsonObject, String> {
        val obj = JsonObject()

        for (subcategory in category.getSubcategories()) {
            val pair = serializeCategory(subcategory)
            obj.put(subcategory.key, pair.first, pair.second)
        }

        for (option in category.getOptions()) {
            val pair = serializeOption(option)
            obj.put(option.key, pair.first, pair.second)
        }

        return Pair(obj, category.comment)
    }

    private fun serializeOption(option: ConfigOption<*>): Pair<JsonElement, String> {
        val element = jankson.marshaller.serialize(option.get())
        return Pair(element, option.comment)
    }
}