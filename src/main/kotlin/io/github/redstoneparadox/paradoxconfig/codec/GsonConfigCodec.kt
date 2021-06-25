package io.github.redstoneparadox.paradoxconfig.codec

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.redstoneparadox.paradoxconfig.config.CollectionConfigOption
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.config.ConfigOption
import io.github.redstoneparadox.paradoxconfig.config.DictionaryConfigOption
import net.minecraft.util.Identifier

class GsonConfigCodec: ConfigCodec {
    override val fileExtension: String = "json"
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Identifier::class.java, object : TypeAdapter<Identifier>() {
            val EMPTY: Identifier = Identifier("empty")

            override fun write(out: JsonWriter?, value: Identifier?) {
                out?.value(value.toString())
            }

            override fun read(`in`: JsonReader?): Identifier {
                return Identifier.tryParse(`in`?.nextString()) ?: EMPTY
            }
        })
        .setPrettyPrinting()
        .create()

    override fun decode(data: String, config: ConfigCategory) {
        val json = gson.fromJson(data, JsonObject::class.java)

        decodeCategory(json, config)
    }

    private fun decodeCategory(json: JsonObject, category: ConfigCategory) {
        for (subcategory in category.getSubcategories()) {
            val obj = json[subcategory.key]

            if (obj is JsonObject) decodeCategory(obj, subcategory)
        }

        for (option in category.getOptions()) {
            val element = json[option.key]

            if (element != null) decodeOption(element, option)
        }
    }

    private fun decodeOption(json: JsonElement, option: ConfigOption<*>) {
        if (json is JsonArray && option is CollectionConfigOption<*, *>) {
            val collection = mutableListOf<Any>()

            json.forEach { collection.add(gson.fromJson(it, option.getElementKClass().java)) }
            option.set(collection)
        } else if (json is JsonObject && option is DictionaryConfigOption<*, *>) {
            val map = mutableMapOf<String, Any>()

            json.entrySet().forEach { map[it.key] = gson.fromJson(it.value, option.getValueKClass().java) }
            option.set(map)
        } else {
            val any = gson.fromJson(json, option.getKClass().java)

            option.set(any)
        }
    }

    override fun encode(config: ConfigCategory): String {
        return gson.toJson(encodeCategory(config))
    }

    private fun encodeCategory(category: ConfigCategory): JsonObject {
        val obj = JsonObject()

        for (option in category.getOptions()) {
            val optionElement = encodeOption(option)

            obj.add(option.key, optionElement)
        }

        for (subcategory in category.getSubcategories()) {
            val subcategoryObj = encodeCategory(subcategory)

            obj.add(subcategory.key, subcategoryObj)
        }

        return obj
    }

    private fun encodeOption(option: ConfigOption<*>): JsonElement {
        val any = option.get()

        if (any is Collection<*>) {
            val array = JsonArray()

            any.forEach { array.add(gson.toJsonTree(it, (option as CollectionConfigOption<*,*>).getElementKClass().java)) }
            return array
        }
        if (any is Map<*, *>) {
            val obj = JsonObject()

            any.keys.forEach { if (it is String) { obj.add(it, gson.toJsonTree(any[it], (option as DictionaryConfigOption<*, *>).getValueKClass().java)) } }
            return obj
        }

        return gson.toJsonTree(any, option.getKClass().java)
    }
}