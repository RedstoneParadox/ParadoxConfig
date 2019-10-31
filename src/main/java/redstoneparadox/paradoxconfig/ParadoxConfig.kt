package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.config.AbstractConfig
import redstoneparadox.paradoxconfig.misc.runTests
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import java.io.File
import java.io.FileNotFoundException
import kotlin.reflect.full.createInstance

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

            data.add(ConfigData(configNames, mod.metadata.name))
        }
    }

    return data;
}

internal fun initConfigs() {
    if (initialized) return

    val configData = getConfigData()
    val baseClass = AbstractConfig::class

    for (data in configData) {
        for (configName in data.configNames) {
            val config = Class.forName(configName).kotlin.objectInstance
            if (config is AbstractConfig) {
                config.init()

                val serializer = config.serializer
                val deserializer = config.deserializer
                val configFile = File(FabricLoader.getInstance().configDirectory, config.file)

                try {
                    val configString = configFile.readText()
                    if (deserializer.receiveSource(configString)) config.deserialize(deserializer)
                } catch (e: FileNotFoundException) {
                    println("Config file for $configName not found so a new one will be created.")
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

internal class ConfigData(val configNames: Collection<String>, val modName: String)

