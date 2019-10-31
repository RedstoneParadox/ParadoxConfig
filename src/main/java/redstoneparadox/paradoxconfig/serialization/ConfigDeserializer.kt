package redstoneparadox.paradoxconfig.serialization

/**
 * Classes that implement this are used to deserialize raw config data
 * into an intermediate representation such as GSON objects and provide
 * information to the config class about option values during
 * initialization.
 */
interface ConfigDeserializer {

    /**
     *  Used to pass the source to your deserializer class.
     *
     *  @param source The contents of the config
     *  file in string form.
     */
    fun receiveSource(source: String)

    /**
     * Used by the config to get the serialized value of an option
     * during initialization.
     *
     * @param key The full config key for an option in the form of
     * a String [Collection]. The very last element represents the
     * key for the option itself while the preceding elements
     * (if any) represent the sub-categories in order.
     *
     * @return The value of the config option. Can be null.
     */
    fun getSetting(key: Collection<String>): Any?
}