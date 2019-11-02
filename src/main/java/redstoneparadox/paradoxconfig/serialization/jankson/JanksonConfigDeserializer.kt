package redstoneparadox.paradoxconfig.serialization.jankson

import blue.endless.jankson.JsonArray
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import blue.endless.jankson.impl.SyntaxError
import io.github.cottonmc.jankson.JanksonFactory
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import java.util.Stack

@Suppress("unused")
class JanksonConfigDeserializer: ConfigDeserializer {

    private var currentObject: JsonObject? = null
    private val objectStack: Stack<JsonObject?> = Stack()

    override fun receiveSource(source: String): Boolean {
        if (source.isNotEmpty()) {
            try {
                currentObject = JanksonFactory
                    .createJankson()
                    .load(source)

                return true
            } catch (e: SyntaxError) {
                println("Encountered error while reading config.")
            }
        }
        return false
    }

    override fun enterCategory(key: String) {
        val subcategory = currentObject?.get(key)

        if (subcategory is JsonObject) {
            objectStack.push(currentObject)
            currentObject = subcategory
            return
        }
        objectStack.push(null)
    }

    override fun exitCategory() {
        currentObject = objectStack.pop()
    }

    override fun readOption(key: String): Any? {
        val option = currentObject?.get(key)

        if (option is JsonPrimitive) {
            return option.value
        }
        return null
    }

    override fun readCollectionOption(key: String): MutableCollection<Any>? {
        val option = currentObject?.get(key)

        if (option is JsonArray) {
            val collection = mutableListOf<Any>()
            for (element in option) {
                if (element is JsonPrimitive) {
                    collection.add(element.value)
                }
            }
            return collection
        }
        return null
    }

    override fun clear() {
        currentObject = null
        objectStack.clear()
    }
}