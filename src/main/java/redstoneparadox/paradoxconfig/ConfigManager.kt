package redstoneparadox.paradoxconfig

import me.sargunvohra.mcmods.autoconfig1u.serializer.ConfigSerializer
import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.config.RootConfigCategory
import redstoneparadox.paradoxconfig.util.NewConfigData
import java.io.File
import java.io.FileNotFoundException

object ConfigManager {
    private val CONFIGS: MutableMap<String, RootConfigCategory> = mutableMapOf()

    internal fun initConfig(rootPackage: String, configNames: Collection<String>, modid: String) {
        for (name in configNames) {
            val className = "${rootPackage}.$name"

            when (val config = Class.forName(className).kotlin.objectInstance) {
                is RootConfigCategory -> {
                    config.init()
                    CONFIGS["$modid:${config.file}"] = config
                    loadConfig(config, modid)
                }
                null -> ParadoxConfig.error("$className could not be found.")
                else -> ParadoxConfig.error("$className does not extend RootConfigCategory")
            }
        }
    }

    internal fun initConfig(data: NewConfigData) {
        for (name in data.configNames) {
            val className = "${data.rootPackage}.$name"

            when (val config = Class.forName(className).kotlin.objectInstance) {
                is RootConfigCategory -> {
                    config.init()
                    CONFIGS["${data.modid}:${config.file}"] = config
                    loadConfig(config, data.modid)
                }
                null -> ParadoxConfig.error("$className could not be found.")
                else -> ParadoxConfig.error("$className does not extend RootConfigCategory")
            }
        }
    }

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
}