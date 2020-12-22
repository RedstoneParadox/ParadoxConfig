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

    fun decode(string: String, config: ConfigCategory)

    fun encode(config: ConfigCategory): String

    companion object {
        private val CODECS: MutableMap<String, ConfigCodec> = mutableMapOf()

        fun addFormat(configCodec: ConfigCodec) {
            val ext = configCodec.fileExtension

            if (CODECS.containsKey(ext)) {
                throw Exception("ConfigCodec for file format $ext was already registered!")
            }

            CODECS[configCodec.fileExtension] = configCodec
        }

        fun getCodec(ext: String): ConfigCodec {
            return CODECS[ext].unwrap(Exception("ConfigCodec for file format $ext was never registered!"))
        }
    }
}