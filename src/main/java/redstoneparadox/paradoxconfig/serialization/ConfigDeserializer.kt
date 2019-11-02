package redstoneparadox.paradoxconfig.serialization

import kotlin.reflect.KClass

/**
 * Classes that implement this are used to deserialize a string
 * representation of the config into an intermediate
 * representation. The config then uses this class to read
 * option values.
 *
 * Note that any classes that implement this interface must contain a
 * 0-parameter constructor
 */
interface ConfigDeserializer {

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
     * Called to enter a sub-category in the current
     * category. Note that this will not be called
     * for the outermost category as it is expected
     * that the deserializer will already be there.
     *
     * @param key The lookup key of the category.
     */
    fun enterCategory(key: String)

    /**
     * Called to exit the current sub-category and
     * return to the outer category.
     */
    fun exitCategory()

    /**
     * Called to read the value of an option in the
     * current category.
     */
    fun readOption(key: String): Any?


    fun readCollectionOption(key: String): Collection<Any>?

    /**
     * Used to clear the current contents of the deserializer
     * when it's used for more than one config file.
     */
    fun clear()
}