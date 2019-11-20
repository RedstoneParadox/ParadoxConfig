package redstoneparadox.paradoxconfig.serialization

import kotlin.reflect.KClass

/**
 * Classes that implement this are used to serialize config options from
 * the config class and transform them into an intermediate
 * representation. The class itself is then responsible for transforming
 * the data into a string representation.
 *
 * @param E The base element type of the intermediate representation.
 * If you were working with GSON, for example, this would be
 * [com.google.gson.JsonElement].
 */
interface ConfigSerializer<E: Any> {
    val eClass: KClass<E>

    /**
     * Called by the config to serialize a value to
     * an instance of [E].
     *
     * @param value The value to serialize.
     *
     * @return An instance of [E] or null if the
     * [value] could not be serialized.
     */
    fun trySerialize(value: Any): E?

    /**
     * Creates a [MutableCollection] that can hold
     * elements of type [E]. The collection itself
     * should extend [E] (this is not enforced
     * with generics due to limitations).
     *
     * @return A [MutableCollection]
     */
    fun createCollection(): MutableCollection<E>

    /**
     * Creates a [MutableMap] with [String] keys
     * a values of type [E]. The map itself
     * should extend [E] (this is not enforced
     * with generics due to limitations).
     *
     * @return a [MutableMap]
     */
    fun createDictionary(): MutableMap<String, E>

    /**
     * Called to add a sub-category to the current category.
     *
     * @param key The category key
     * @param comment The category's comment.
     */
    fun addCategory(key: String, comment: String)

    /**
     * Called to return to the outer category.
     */
    fun exitCategory()

    /**
     * Called to write an option value to the config.
     *
     * @param key The key for this option in the current
     * category.
     * @param value The value to write.
     * @param comment The comment for this option.
     */
    fun writeValue(key: String, value: E, comment: String)

    @Deprecated("No longer called.")
    fun writeOption(key: String, value: Any, comment: String) {
        
    }

    /**
     * Called when the config class has finished passing data
     * to the serialize so that it can save the config data
     * to the file.
     *
     * @return The string-representation of the config.
     */
    fun complete(): String

    /**
     * Used to clear the current contents of the serializer when
     * it's used for more than one config file.
     */
    fun clear()
}