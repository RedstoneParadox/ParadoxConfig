package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.config.RootConfigCategory
import redstoneparadox.paradoxconfig.test.runTests
import java.io.File
import java.io.FileNotFoundException

internal var initialized: Boolean = false

@Suppress("unused")
fun init() {
    if (FabricLoader.getInstance().isDevelopmentEnvironment) {
        runTests()
    }
}

internal fun getConfigData(): Collection<ConfigData> {
    val data = arrayListOf<ConfigData>();

    for (mod in FabricLoader.getInstance().allMods) {
        if (mod.metadata.containsCustomValue("paradoxconfig")) {
            val classNames = mod.metadata.getCustomValue("paradoxconfig").asArray

            val configNames = arrayListOf<String>()
            for (className in classNames) {
                configNames.add(className.asString)
            }

            if (mod.metadata.id == "paradoxconfig" && !FabricLoader.getInstance().isDevelopmentEnvironment) continue

            data.add(ConfigData(configNames, mod.metadata.id))
        }
    }

    return data;
}

internal fun initConfigs() {
    if (initialized) return

    val configData = getConfigData()
    val baseClass = RootConfigCategory::class

    for (data in configData) {
        for (configName in data.configNames) {
            val config = Class.forName(configName).kotlin.objectInstance
            if (config is RootConfigCategory) {
                config.init()

                val serializer = config.serializer
                val deserializer = config.deserializer
                val configFile = File(FabricLoader.getInstance().configDirectory, config.file)

                try {
                    val configString = configFile.readText()
                    if (deserializer.receiveSource(configString)) config.deserialize(deserializer)
                } catch (e: FileNotFoundException) {
                    println("Config file for ${config.file} not found so a new one will be created.")
                }
                config.serialize(serializer)
                val configString = serializer.complete()
                configFile.writeText(configString)
            }
            else {
                println("Object $configName either doesn't extend ${baseClass.simpleName} or is not an object.")
            }
        }
    }

    initialized = true
}

internal class ConfigData(val configNames: Collection<String>, val modid: String)

