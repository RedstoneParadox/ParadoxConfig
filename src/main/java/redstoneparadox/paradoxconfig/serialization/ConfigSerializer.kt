package redstoneparadox.paradoxconfig.serialization

import kotlin.reflect.KClass

/**
 * Classes that implement this are used to serialize config options from
 * the config class and transform them into an intermediate
 * representation. The class itself is then responsible for transforming
 * the data into a string representation.
 */
interface ConfigSerializer<E> {

    fun serializeValue(value: Any): E?

    fun createCollection(): MutableCollection<E>

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

    fun putValue(key: String, value: E, comment: String)

    /**
     * Called to write an option to the intermediate
     * representation.
     *
     * @param key The option key.
     * @param value The value of the option.
     * @param comment The comment for this option.
     */
    fun writeOption(key: String, value: Any, comment: String)

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