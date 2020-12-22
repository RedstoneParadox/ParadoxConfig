package io.github.goconfigure.paradoxconfig

/**
 * Entrypoint invoked before Paradox Config loads config files.
 *
 * This entrypoint should be used only for [io.github.goconfigure.paradoxconfig.codec.ConfigCodec]
 * implementations to support new file formats. You should not
 * mess with game state or anything else here.
 *
 * This entrypoint is exposed as [pconfigFormat] in the mod
 * json and runs for every environment.
 */
interface ConfigFormatInitializer {
    fun initializeConfigFormat()
}