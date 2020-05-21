package io.github.redstoneparadox.paradoxconfig.serialization.jankson

import blue.endless.jankson.*
import io.github.cottonmc.jankson.JanksonFactory
import net.minecraft.util.Identifier
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import java.util.*
import kotlin.reflect.KClass

class JanksonConfigSerializer: ConfigSerializer<JsonElement> {
    override val eClass: KClass<JsonElement> = JsonElement::class

    var currentObject: JsonObject = JsonObject()
    val objectStack: Stack<JsonObject> = Stack()

    private val jankson = JanksonFactory.createJankson()

    override fun trySerialize(value: Any): JsonElement? {
        return when (value) {
            is String, is Char, is Byte, is Short, is Int, is Long, is Float, is Double, is Boolean -> JsonPrimitive(value)
            is Identifier -> JsonPrimitive(value.toString())
            else -> null
        }
    }

    override fun createCollection(): MutableCollection<JsonElement> = JsonArray()

    override fun createDictionary(): MutableMap<String, JsonElement> = JsonObject()

    override fun addCategory(key: String, comment: String) {
        val newObject = JsonObject()
        currentObject.put(key, newObject, comment)
        objectStack.push(currentObject)
        currentObject = newObject
    }

    override fun exitCategory() {
        currentObject = objectStack.pop()
    }

    override fun writeValue(key: String, value: JsonElement, comment: String) {
        currentObject.put(key, value, comment)
    }

    override fun complete(): String {
        return currentObject.toJson(true, true)
    }

    override fun clear() {
        currentObject = JsonObject()
        objectStack.clear()
    }
}