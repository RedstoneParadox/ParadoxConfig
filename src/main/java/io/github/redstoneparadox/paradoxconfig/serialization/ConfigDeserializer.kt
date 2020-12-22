package io.github.redstoneparadox.paradoxconfig.serialization

import kotlin.reflect.KClass

/**
 * Classes that implement this are used to deserialize a string
 * representation of the config into an intermediate
 * representation. The config then uses this class to read
 * option values.
 *
 * @param E The base element type of the intermediate representation.
 * If you were working with GSON, for example, this would be
 * [com.google.gson.JsonElement].
 */
@Deprecated("Not used by new serialization system.")
interface ConfigDeserializer<E: Any> {
    val eClass: KClass<E>

    /**
     *  Used to pass the source to your deserializer class.
     *
     *  @param source The contents of the config
     *  file in string form.
     *
     *  @return Whether or not the config was
     *  successfully converted to an intermediary
     *  representation.
     */
    fun receiveSource(source: String): Boolean

    /**
     * Called to covert an element of type [E] to a
     * value of type [R].
     *
     * @param e The element to deserialize.
     * @param rClass The class to deserialize to.
     *
     * @return The deserialized value or null if
     * deserialization was not successful
     */
    fun <R: Any> tryDeserialize(e: E, rClass: KClass<R>): R?

    /**
     * Called to enter a sub-category in the current
     * category. Note that this will not be called
     * for the outermost category as it is expected
     * that the deserializer will already be there.
     *
     * @param key The key for this category in the
     *            current category.
     */
    fun enterCategory(key: String)

    /**
     * Called to exit the current sub-category and
     * return to the outer category.
     */
    fun exitCategory()

    /**
     * Called to read an option value. Should return the
     * value as-is.
     *
     * @param key The key for this option
     *
     * @return A value of type [E] or null if the option
     * is not present.
     */
    fun readValue(key: String): E?

    @Deprecated("No longer called.")
    fun readOption(key: String): Any? {
        return null
    }

    @Deprecated("No longer called.")
    fun readCollectionOption(key: String): Collection<Any>? {
        return null
    }

    @Deprecated("No longer called.")
    fun readDictionaryOption(key: String): Map<Any, Any>? {
        return null
    }

    /**
     * Used to clear the current contents of the deserializer
     * when it's used for more than one config file.
     */
    fun clear()
}