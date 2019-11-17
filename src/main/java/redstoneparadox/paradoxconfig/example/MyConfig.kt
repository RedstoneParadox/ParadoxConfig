package redstoneparadox.paradoxconfig.example

import blue.endless.jankson.JsonElement
import redstoneparadox.paradoxconfig.config.ConfigCategory
import redstoneparadox.paradoxconfig.config.RootConfigCategory
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import redstoneparadox.paradoxconfig.serialization.jankson.JanksonConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.jankson.JanksonConfigSerializer

object MyConfig: RootConfigCategory("myconfig.json5") {
    override val serializer: ConfigSerializer<JsonElement> = JanksonConfigSerializer()
    override val deserializer: ConfigDeserializer<JsonElement> = JanksonConfigDeserializer()

    var bool: Boolean by option(true, "bool")

    var number: Long by option(1L, "int")

    // Fun fact, this calls a different overload of the option function
    var ranged: Long by option(1L, 1L..3L, "ranged")

    // So does this.
    var fruits: MutableList<String> by option(mutableListOf("pear", "apple", "orange", "tomato"), "option")

    // And so does this.
    var dictionary: HashMap<String, Long> by option(hashMapOf("one" to 1L, "two" to 2L, "three" to 3L), "dictionary")

    object Greetings: ConfigCategory("my_sub_category", "A Bunch of Greetings!") {
        var english: MutableList<String> by option(mutableListOf("Hi!", "Hey!"), "english")
        var spanish: MutableList<String> by option(mutableListOf("Hola!"), "spanish")
    }
}