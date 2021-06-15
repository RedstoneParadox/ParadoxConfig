package io.github.redstoneparadox.paradoxconfig

import io.github.redstoneparadox.paradoxconfig.codec.ConfigCodec
import io.github.redstoneparadox.paradoxconfig.codec.GsonConfigCodec
import io.github.redstoneparadox.paradoxconfig.codec.JanksonConfigCodec
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.apache.logging.log4j.LogManager

object ParadoxConfig: PreLaunchEntrypoint {
    const val MOD_ID: String = "paradoxconfig"
    private const val MOD_NAME = "Paradox Config"
    private val LOGGER = LogManager.getFormatterLogger(MOD_NAME)

    @Suppress("unused")
    override fun onPreLaunch() {
        val loader = FabricLoader.getInstance()

        ConfigCodec.addCodec(GsonConfigCodec())
        
        if (loader.isModLoaded("jankson")) {
            ConfigCodec.addCodec(JanksonConfigCodec())
        }

        for (entrypoint in loader.getEntrypoints("pconfigFormat", ConfigFormatInitializer::class.java)) {
            entrypoint.initializeConfigFormat()
        }

        for (mod in loader.allMods) {
            if (mod.metadata.containsCustomValue(MOD_ID)) {
                val obj = mod.metadata.getCustomValue(MOD_ID).asObject
                val rootPackage = obj.get("package").asString
                val classNames = obj.get("configs").asArray.map { it.asString }

                if (mod.metadata.id == MOD_ID && !FabricLoader.getInstance().isDevelopmentEnvironment) continue

                ConfigManager.initConfigs(rootPackage, classNames, mod.metadata.id)
            }
        }
    }

    internal fun log(msg: String) {
        LOGGER.info("[$MOD_NAME] $msg")
    }

    internal fun warn(msg: String) {
        LOGGER.warn("[$MOD_NAME] $msg")
    }

    internal fun error(msg: String) {
        LOGGER.warn("[$MOD_NAME] $msg")
    }
}

