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

    fun read(string: String, config: ConfigCategory)

    fun write(config: ConfigCategory): String

    companion object {
        private val FORMATS: MutableMap<String, ConfigCodec> = mutableMapOf()

        fun addFormat(configCodec: ConfigCodec) {
            val ext = configCodec.fileExtension

            if (FORMATS.containsKey(ext)) {
                throw Exception("ConfigIO for file format $ext was already registered!")
            }

            FORMATS[configCodec.fileExtension] = configCodec
        }

        fun getConfigIO(ext: String): ConfigCodec {
            return FORMATS[ext].unwrap(Exception("ConfigIO for file format $ext was never registered!"))
        }
    }
}