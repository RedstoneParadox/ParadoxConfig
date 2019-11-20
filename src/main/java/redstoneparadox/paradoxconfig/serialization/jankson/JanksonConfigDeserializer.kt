package redstoneparadox.paradoxconfig.serialization.jankson

import blue.endless.jankson.*
import blue.endless.jankson.impl.SyntaxError
import io.github.cottonmc.jankson.JanksonFactory
import net.minecraft.util.Identifier
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

@Suppress("unused")
class JanksonConfigDeserializer: ConfigDeserializer<JsonElement> {
    override val eClass: KClass<JsonElement> = JsonElement::class

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

    override fun <R: Any> tryDeserialize(e: JsonElement, rClass: KClass<R>): R? {
        if (e is JsonPrimitive) {
            val any = when (val value = e.value) {
                is Char, is Double, is Long -> value
                is Byte, is Short, is Int -> (value as Number).toLong()
                is Float -> value.toDouble()
                is String -> if (rClass == Identifier::class) Identifier.tryParse(value) else value
                else -> null
            }
            if (rClass.isInstance(any)) return rClass.cast(any)
        }
        return null
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

    override fun readValue(key: String): JsonElement? = currentObject?.get(key)

    override fun clear() {
        currentObject = null
        objectStack.clear()
    }
}