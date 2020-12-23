package io.github.redstoneparadox.paradoxconfig

import io.github.redstoneparadox.paradoxconfig.codec.ConfigCodec
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.config.RootConfigCategory
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileNotFoundException

object ConfigManager {
    @Deprecated("Uses old serialization system.")
    private val OLD_CONFIGS: MutableMap<String, RootConfigCategory> = mutableMapOf()
    private val CONFIGS: MutableMap<String, ConfigCategory> = mutableMapOf()

    fun getConfig(id: String): ConfigCategory? {
        return OLD_CONFIGS[id] ?: CONFIGS[id]
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
                is RootConfigCategory -> {
                    config.init()
                    OLD_CONFIGS["$modid:${config.file}"] = config
                    loadConfig(config, modid)
                    ParadoxConfig.warn("$className extends RootConfigCategory, which is deprecated.")
                }
                null -> ParadoxConfig.error("$className could not be found.")
                else -> ParadoxConfig.error("$className does not extend ConfigCategory")
            }
        }
    }

    @Deprecated("Uses old serialization system.")
    private fun loadConfig(config: RootConfigCategory, modid: String) {
        val serializer = config.serializer
        val deserializer = config.deserializer
        val configFile = File(FabricLoader.getInstance().configDirectory, "${modid}/${config.file}")

        try {
            val configString = configFile.readText()
            if (deserializer.receiveSource(configString)) config.deserialize(deserializer)
        } catch (e: FileNotFoundException) {
            ParadoxConfig.log("Config file $modid:${config.file} was not found; a new one will be created.")
            try {
                configFile.parentFile.mkdirs()
            } catch (e: SecurityException) {
                ParadoxConfig.error("Could not create config file $modid:${config.file} due to security issues.")
                e.printStackTrace()
                return
            }
        }
        config.serialize(serializer)
        val configString = serializer.complete()
        configFile.writeText(configString)
    }

    private fun loadConfig(config: ConfigCategory, modid: String) {
        val ext = config.key.split('.').last()
        val configCoded = ConfigCodec.getCodec(ext)
        val file = File(FabricLoader.getInstance().configDirectory, "${modid}/${config.key}")

        if (file.exists()) {
            try {
                val configData = file.readText()
                configCoded.decode(configData, config)
                file.writeText(configCoded.encode(config))
            } catch (e: Exception) {
                ParadoxConfig.error("Could not write to config file $modid:${config.key} due to an exception.")
                e.printStackTrace()
            }
        }
        else {
            ParadoxConfig.log("Config file $modid:${config.key} was not found; a new one will be created.")
            try {
                file.parentFile.mkdirs()
                file.createNewFile()
                file.writeText(configCoded.encode(config))
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