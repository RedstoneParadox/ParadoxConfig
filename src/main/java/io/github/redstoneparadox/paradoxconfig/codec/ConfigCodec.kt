package io.github.redstoneparadox.paradoxconfig.codec

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.util.unwrap

/**
 * Classes that implement this interface are used to read
 * and write config files using a specific format.
 *
 * Implementors should not do any saving or loading
 * themselves.
 */
interface ConfigCodec {
    /**
     * Function to get the file extension
     * for this ConfigCodec.
     */
    val fileExtension: String

    /**
     * Reads config option data from the string to
     * the passed config.
     *
     * @param config The config to decode to.
     * @param data The formatted data to be decoded.
     */
    fun decode(data: String, config: ConfigCategory)

    /**
     * Converts a config to the codec's specified
     * file format.
     *
     * @param config the config to encode.
     * @return The encoded config as a string.
     */
    fun encode(config: ConfigCategory): String

    companion object {
        private val CODECS: MutableMap<String, ConfigCodec> = mutableMapOf()

        /**
         * Adds a new codec for a file format.
         *
         * @param configCodec the [ConfigCodec] to add.
         */
        fun addCodec(configCodec: ConfigCodec) {
            val ext = configCodec.fileExtension

            if (CODECS.containsKey(ext)) {
                throw Exception("ConfigCodec for file format $ext was already registered!")
            }

            CODECS[ext] = configCodec
        }

        fun getCodec(ext: String): ConfigCodec {
            return CODECS[ext].unwrap(Exception("ConfigCodec for file format $ext was never registered!"))
        }
    }
}