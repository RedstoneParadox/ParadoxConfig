package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.config.AbstractConfig
import redstoneparadox.paradoxconfig.config.ConfigCategory
import redstoneparadox.paradoxconfig.misc.runTests
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer
import java.io.File
import java.io.FileNotFoundException
import kotlin.reflect.full.createInstance

@Suppress("unused")
fun init() {
    newInitConfig()

    if (FabricLoader.getInstance().isDevelopmentEnvironment) {
        runTests()
    }
}

internal fun getConfigData(): Collection<ConfigData> {
    val data = arrayListOf<ConfigData>();

    for (mod in FabricLoader.getInstance().allMods) {
        if (mod.metadata.containsCustomValue("paradoxconfig")) {
            val configData = mod.metadata.getCustomValue("paradoxconfig").asObject
            val serializerName = configData["serializer"].asString
            val deserializerName = configData["deserializer"].asString
            val classNames = configData["classes"].asArray

            val configNames = arrayListOf<String>()
            for (className in classNames) {
                configNames.add(className.asString)
            }

            data.add(ConfigData(serializerName, deserializerName, configNames, mod.metadata.name))
        }
    }

    return data;
}

internal fun newInitConfig() {
    val configData = getConfigData()
    val baseClass = AbstractConfig::class

    for (data in configData) {
        try {
            val serializer = Class.forName(data.serializerName).kotlin.createInstance()
            val deserializer = Class.forName(data.deserializerName).kotlin.createInstance()

            if (serializer !is ConfigSerializer || deserializer !is ConfigDeserializer) {
                println("Could not find serializer or deserializer for mod ${data.modName}. Configs for ${data.modName} will not be loaded.")
                continue
            }

            for (configName in data.configNames) {
                val config = Class.forName(configName).kotlin.objectInstance

                if (config is AbstractConfig) {
                    config.init()

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
        } catch (e: ClassNotFoundException) {
            println("Could not find serializer or deserializer for mod ${data.modName}. Configs for ${data.modName} will not be loaded.")
        }
    }
}

internal class ConfigData(val serializerName: String, val deserializerName: String, val configNames: Collection<String>, val modName: String)

