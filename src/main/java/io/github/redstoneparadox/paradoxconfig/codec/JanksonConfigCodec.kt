package io.github.redstoneparadox.paradoxconfig.codec

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonArray
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import io.github.cottonmc.jankson.JanksonFactory
import io.github.redstoneparadox.paradoxconfig.config.CollectionConfigOption
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.config.ConfigOption
import io.github.redstoneparadox.paradoxconfig.config.DictionaryConfigOption

class JanksonConfigCodec: ConfigCodec {
    override val fileExtension: String = "json5"
    private val jankson: Jankson

    init {
        val builder = JanksonFactory.builder()
        jankson = builder.build()
    }

    override fun decode(data: String, config: ConfigCategory) {
        val json = jankson.load(data)
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
        if (json is JsonArray && option is CollectionConfigOption<*, *>) {
            val collection = mutableListOf<Any>()
            json.forEach { collection.add(jankson.marshaller.marshall(option.getElementKClass().java, it)) }
            option.set(collection)
        }
        else if (json is JsonObject && option is DictionaryConfigOption<*, *>) {
            val map = mutableMapOf<String, Any>()
            json.forEach { map[it.key] = jankson.marshaller.marshall(option.getValueKClass().java, it.value) }
            option.set(map)
        }
        else {
            val any = jankson.marshaller.marshall(option.getKClass().java, json)
            option.set(any)
        }
    }

    override fun encode(config: ConfigCategory): String {
        return encodeCategory(config).first.toJson(true, true)
    }

    private fun encodeCategory(category: ConfigCategory): Pair<JsonObject, String> {
        val obj = JsonObject()

        for (subcategory in category.getSubcategories()) {
            val pair = encodeCategory(subcategory)
            obj.put(subcategory.key, pair.first, pair.second)
        }

        for (option in category.getOptions()) {
            val pair = encodeOption(option)
            obj.put(option.key, pair.first, pair.second)
        }

        return Pair(obj, category.comment)
    }

    private fun encodeOption(option: ConfigOption<*>): Pair<JsonElement, String> {
        val any = option.get()

        if (any is Collection<*>) {
            val array = JsonArray()
            any.forEach { array.add(jankson.marshaller.serialize(it)) }
            return Pair(array, option.comment)
        }
        if (any is Map<*, *>) {
            val obj = JsonObject()
            any.keys.forEach { if (it is String) { obj[it] = jankson.marshaller.serialize(any[it]) } }
            return Pair(obj, option.comment)
        }

        val element = jankson.marshaller.serialize(any)
        return Pair(element, option.comment)
    }
}