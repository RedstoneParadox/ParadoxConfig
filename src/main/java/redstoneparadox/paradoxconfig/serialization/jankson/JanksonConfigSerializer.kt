package redstoneparadox.paradoxconfig.serialization.jankson

import blue.endless.jankson.JsonObject
import io.github.cottonmc.jankson.JanksonFactory
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import java.util.*

class JanksonConfigSerializer: ConfigSerializer {

    var currentObject: JsonObject = JsonObject()
    val objectStack: Stack<JsonObject> = Stack()

    private val jankson = JanksonFactory.createJankson()

    override fun addCategory(key: String, comment: String) {
        val newObject = JsonObject()
        currentObject.put(key, newObject, comment)
        objectStack.push(currentObject)
        currentObject = newObject
    }

    override fun exitCategory() {
        currentObject = objectStack.pop()
    }

    override fun writeOption(key: String, value: Any, comment: String) {
        currentObject.put(key, jankson.toJson(value), comment)
    }

    override fun complete(): String {
        return currentObject.toJson(true, true)
    }

    override fun clear() {
        currentObject = JsonObject()
        objectStack.clear()
    }
}