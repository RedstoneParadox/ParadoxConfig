package io.github.redstoneparadox.paradoxconfig.io

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.util.unwrap

/**
 * Classes that implement this interface are used to read
 * and write config files using a specific format.
 *
 * Implementors should not do any saving or loading
 * themselves.
 */
interface ConfigIO {

    /**
     * Function to get the file extension
     * for this ConfigIO.
     */
    val fileExtension: String

    fun read(string: String, config: ConfigCategory)

    fun write(config: ConfigCategory): String

    companion object {
        private val FORMATS: MutableMap<String, ConfigIO> = mutableMapOf()

        fun addFormat(configIO: ConfigIO) {
            val ext = configIO.fileExtension

            if (FORMATS.containsKey(ext)) {
                throw Exception("ConfigIO for file format $ext was already registered!")
            }

            FORMATS[configIO.fileExtension] = configIO
        }

        fun getConfigIO(ext: String): ConfigIO {
            return FORMATS[ext].unwrap(Exception("ConfigIO for file format $ext was never registered!"))
        }
    }
}