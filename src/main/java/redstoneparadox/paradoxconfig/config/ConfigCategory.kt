package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass

/**
 * Inheritors of this class represent a category in your config file. For the
 * root category, please extend [RootConfigCategory] instead so the metadata
 * for saving and loading the config can be provided.
 */
abstract class ConfigCategory(val key : String = "", val comment: String = "") {

    private val optionsMap: HashMap<String, ConfigOption<*>> = HashMap()
    private val categoriesMap: HashMap<String, ConfigCategory> = HashMap()

    internal fun init() {
        val kclass = this::class

        for (innerclass in kclass.nestedClasses) {
            val obj = innerclass.objectInstance
            if (obj is ConfigCategory) {
                categoriesMap[obj.key] = obj
                obj.init()
            }
        }
    }

    internal fun serialize(configSerializer: ConfigSerializer) {
        if (key.isNotEmpty()) configSerializer.addCategory(key, comment)

        for (category in categoriesMap.values) {
            category.serialize(configSerializer)
        }

        for (option in optionsMap.values) {
            option.serialize(configSerializer)
        }

        if (key.isNotEmpty()) configSerializer.exitCategory()
    }

    internal fun deserialize(configDeserializer: ConfigDeserializer) {
        if (key.isNotEmpty()) configDeserializer.enterCategory(key)

        for (category in categoriesMap.values) {
            category.deserialize(configDeserializer)
        }

        for (option in optionsMap.values) {
            option.deserialize(configDeserializer)
        }

        if (key.isNotEmpty()) configDeserializer.exitCategory()
    }

    /**
     * Creates a config option holding a [Boolean] value, a comment, and a config key.
     *
     * @param default The default value.
     * @param key The config key for this option in it's category.
     * @param comment (optional) Adds a short description to the option.
     *
     * @return A [ConfigOption] delegate that holds a [Boolean].
     */
    protected fun option(default: Boolean, key: String, comment: String = ""): ConfigOption<Boolean> {
        return option(boolType, default, key, comment)
    }

    /**
     * Creates a config option holding a [String] value, a comment, and a config key.
     *
     * @param default The default value.
     * @param key The config key for this option in it's category.
     * @param comment (optional) Adds a short description to the option.
     *
     * @return A [ConfigOption] delegate that holds a [String].
     */
    protected fun option(default: String, key: String, comment: String = ""): ConfigOption<String> {
        return option(stringType, default, key, comment)
    }

    /**
     * Creates a config option holding a [Long] value, a comment, and a config key.
     *
     * @param default The default value.
     * @param key The config key for this option in it's category.
     * @param comment (optional) Adds a short description to the option.
     *
     * @return A [ConfigOption] delegate that holds a [Long].
     */
    protected fun option(default: Long, key: String, comment: String = ""): ConfigOption<Long> {
        return option(longType, default, key, comment)
    }

    /**
     * Creates a config option holding a [Double] value, a comment, and a config key.
     *
     * @param default The default value.
     * @param key The config key for this option in it's category.
     * @param comment (optional) Adds a short description to the option.
     *
     * @return A [ConfigOption] delegate that holds a [Double].
     */
    protected fun option(default: Double, key: String, comment: String = ""): ConfigOption<Double> {
        return option(doubleType, default, key, comment)
    }

    /**
     * Creates a config option holding a [Long] value that's limited to a certain range,
     * a comment, and a config key.
     *
     * @param default The default value.
     * @param key The config key for this option in it's category.
     * @param comment (optional) Adds a short description to the option.
     *
     * @return A [RangeConfigOption] delegate that holds a [Long] and it's valid range.
     */
    protected fun rangedOption(default: Long, range: LongRange, key: String, comment: String = ""): RangeConfigOption<Long> {
        val option = RangeConfigOption(longType, default, key, "$comment [Values: ${range.first} to ${range.last}]", range)
        optionsMap[key] = option
        return option
    }

    private fun <T : Any> option(type: KClass<T>, default: T, key: String, comment: String): ConfigOption<T> {
        val option = ConfigOption(type, default, key, comment)
        optionsMap[key] = option
        return option
    }

    private companion object {
        val boolType = Boolean::class
        val stringType = String::class
        val longType = Long::class
        val doubleType = Double::class
    }
}