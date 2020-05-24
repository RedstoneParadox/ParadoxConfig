package io.github.redstoneparadox.paradoxconfig.config

import io.github.redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import io.github.redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Inheritors of this class represent a category in your config file. For the
 * root category, please extend [RootConfigCategory] instead so the metadata
 * for saving and loading the config can be provided.
 */
abstract class ConfigCategory(val key : String = "", val comment: String = "") {

    @PublishedApi
    internal val optionsMap: HashMap<String, ConfigOption<*>> = HashMap()
    private val categoriesMap: HashMap<String, ConfigCategory> = HashMap()

    internal val optionsList: MutableList<ConfigOption<*>> = mutableListOf()
    private val categoryList: MutableList<ConfigCategory> = mutableListOf()

    internal fun init() {
        val kclass = this::class

        for (innerclass in kclass.nestedClasses) {
            val category = innerclass.objectInstance
            if (category is ConfigCategory) {
                categoriesMap[category.key] = category
                categoryList.add(category)
                category.init()
            }
        }

        for (property in kclass.declaredMemberProperties) {
            property.isAccessible = true
            val delegate = (property as KProperty1<ConfigCategory, *>).getDelegate(this)
            if (delegate is ConfigOption<*>) {
                optionsMap[delegate.key] = delegate
                optionsList.add(delegate)
            }
        }
    }

    internal fun serialize(configSerializer: ConfigSerializer<*>) {
        if (key.isNotEmpty()) configSerializer.addCategory(key, comment)

        for (option in optionsList) {
            option.serialize(configSerializer)
        }

        for (category in categoryList) {
            category.serialize(configSerializer)
        }

        if (key.isNotEmpty()) configSerializer.exitCategory()
    }

    internal fun deserialize(configDeserializer: ConfigDeserializer<*>) {
        if (key.isNotEmpty()) configDeserializer.enterCategory(key)

        for (option in optionsList) {
            option.deserialize(configDeserializer)
        }

        for (category in categoryList) {
            category.deserialize(configDeserializer)
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
        val kClass = T::class
        return ConfigOption(kClass, default, key, "$comment [Values: ${getPossibleValues(kClass)}]")
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
        val kClass = T::class
        return RangeConfigOption(kClass, default, key, "$comment [Values: ${getPossibleValues(kClass)} in $range]", range)
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
        val kClass = T::class
        return CollectionConfigOption(kClass, U::class, "$comment [Collection of ${getPossibleValues(kClass)}]", key, default)
    }

    /**
     * Creates a config option of a [MutableMap] with keys of type
     * [K] and values of type [V]
     *
     * @param default The default value for this option.
     * @param key the config key for this option.
     * @param comment (optional) The comment for this option.
     *
     * @return A [DictionaryConfigOption] holding an implementation of
     * [MutableMap] with keys of type [K] to values of type [V]
     */
    protected inline fun <reified  V: Any, reified T: MutableMap<String, V>> option(default: T, key: String, comment: String = ""): DictionaryConfigOption<V, T> {
        val valueClass = V::class
        return DictionaryConfigOption(valueClass, T::class, default, key, "$comment [Keys: any string, Values: ${getPossibleValues(valueClass)}]")
    }

    operator fun get(key: String): Any? {
        return get(key.splitToSequence("."))
    }

    private fun get(key: Sequence<String>): Any? {
        val first = key.first()
        return if (key.last() == first) {
            optionsMap[first]?.get()
        } else {
            categoriesMap[first]?.get(key.drop(1))
        }
    }

    operator fun set(key: String, value: Any) {
        set(key.splitToSequence("."), value)
    }

    private fun set(key: Sequence<String>, value: Any) {
        val first = key.first()
        if (key.last() == first) {
            optionsMap[first]?.set(value)
        }
        else {
            categoriesMap[first]?.set(key.drop(1), value)
        }
    }

    @PublishedApi
    internal inline fun <reified T: Any> getPossibleValues(kclass: KClass<T>): String {
        return when (kclass) {
            Boolean::class -> "true/false"
            String::class -> "any string"
            Char::class -> "any character"
            Byte::class, Short::class, Int::class, Long::class -> "any number"
            Float::class, Double::class -> "any decimal number"
            else -> "unknown"
        }
    }
}