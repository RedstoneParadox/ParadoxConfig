package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer

/**
 * Inheritors of this class represent a category in your config file. For the
 * root category, please extend [RootConfigCategory] instead so the metadata
 * for saving and loading the config can be provided.
 */
abstract class ConfigCategory(val key : String = "", val comment: String = "") {

    @PublishedApi
    internal val optionsMap: HashMap<String, ConfigOption<*>> = HashMap()
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
     * Creates a config option holding a value of type [T].
     *
     * @param default The default value for this option.
     * @param key The config key for this option.
     * @param comment (optional) a comment for this option.
     *
     * @return A [ConfigOption] delegate that holds option values of type [T].
     */
    protected inline fun <reified T: Any> option(default: T, key: String, comment: String = ""): ConfigOption<T> {
        val option = ConfigOption(T::class, default, key, comment)
        optionsMap[key] = option
        return option
    }

    /**
     * Creates a config option holding a value of type [T] which is bounded by
     * a [ClosedRange]. Note that [T] must extend [Comparable]
     *
     * @param default The default value for this option.
     * @param range The range to bound this option to.
     * @param key The config key for this option.
     * @param comment The comment for this option.
     *
     * @return A [RangeConfigOption] delegate that holds an option value of type [T].
     */
    protected inline fun <reified T> option(default: T, range: ClosedRange<T>, key: String, comment: String = ""): RangeConfigOption<T> where T: Any, T: Comparable<T> {
        val option = RangeConfigOption(T::class, default, key, comment, range)
        optionsMap[key] = option
        return option
    }

    /**
     * Created a config option of a [MutableCollection] holding values of type.
     * [T].
     *
     * @param default The default value for this option.
     * @param key The config key for this option.
     * @param comment (optional) The comment for this option.
     *
     * @return A [CollectionConfigOption] holding a [MutableCollection]
     * implementer containing values of type [T].
     */
    protected inline fun <reified T: Any, reified U: MutableCollection<T>> option(default: U, key: String, comment: String = ""): CollectionConfigOption<T, U> {
        val option = CollectionConfigOption(T::class, U::class, comment, key, default)
        optionsMap[key] = option
        return option
    }
}