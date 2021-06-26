package io.github.redstoneparadox.paradoxconfig

import io.github.redstoneparadox.paradoxconfig.codec.ConfigCodec
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import net.fabricmc.loader.api.FabricLoader
import java.io.File

object ConfigManager {
    private val CONFIGS: MutableMap<String, ConfigCategory> = mutableMapOf()

    fun getConfig(id: String): ConfigCategory? {
        return CONFIGS[id]
    }

    internal fun initConfigs(rootPackage: String, configNames: Collection<String>, modid: String) {
        for (name in configNames) {
            val className = "${rootPackage}.$name"

            when (val config = Class.forName(className).kotlin.objectInstance) {
                is ConfigCategory -> {
                    config.init()
                    CONFIGS["$modid:${config.key}"] = config
                    loadConfig(config, modid)
                }
                null -> ParadoxConfig.error("$className could not be found.")
                else -> ParadoxConfig.error("$className does not extend ConfigCategory")
            }
        }
    }

    private fun loadConfig(config: ConfigCategory, modid: String) {
        val ext = config.key.split('.').last()
        val configCodec = ConfigCodec.getCodec(ext)
        val file = File(FabricLoader.getInstance().configDirectory, "${modid}/${config.key}")

        if (file.exists()) {
            try {
                val configData = file.readText()

                configCodec.decode(configData, config)
                file.writeText(configCodec.encode(config))
            } catch (e: Exception) {
                ParadoxConfig.error("Could not write to config file $modid:${config.key} due to an exception.")
                e.printStackTrace()
            }
        } else {
            ParadoxConfig.log("Config file $modid:${config.key} was not found; a new one will be created.")

            try {
                file.parentFile.mkdirs()
                file.createNewFile()
                file.writeText(configCodec.encode(config))
            } catch (e: SecurityException) {
                ParadoxConfig.error("Could not create config file $modid:${config.key} due to security issues.")
                e.printStackTrace()
            } catch (e: Exception) {
                ParadoxConfig.error("Could not create config file $modid:${config.key} due to an exception.")
                e.printStackTrace()
            }
        }
    }
}