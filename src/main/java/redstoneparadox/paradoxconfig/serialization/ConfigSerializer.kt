package redstoneparadox.paradoxconfig.serialization

/**
 * Classes that implement this are used to serialize config options from
 * the config class and write them into raw config data.
 */
interface ConfigSerializer {

    /**
     * Used to add a category to the serializer's intermediate
     * representation.
     *
     * @param key The full config key for the category passed
     * as a String [Collection]. Each element of the
     * collection represents sub-categories of the top-most
     * category in order.
     */
    fun writeCategory(key: Collection<String>)

    /**
     * Used to add an option to the serializer's intermediate
     * representation.
     *
     * @param key The full config key for the option passed as
     * a String [Collection]. The last element of the
     * collection represents the key for the option while the
     * preceding elements represent the keys leading to the
     * category this option is contained in.
     */
    fun <T> writeOption(key: Collection<String>, t: T)

    /**
     * Called when the config class has finished passing data
     * to the serialize so that it can save the config data
     * to the file.
     *
     * @return The string-representation of the config.
     */
    fun complete(): String
}