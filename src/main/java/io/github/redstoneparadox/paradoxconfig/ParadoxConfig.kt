package io.github.redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.apache.logging.log4j.LogManager
import io.github.redstoneparadox.paradoxconfig.util.NewConfigData

object ParadoxConfig: PreLaunchEntrypoint {
    const val MOD_ID: String = "pconfig"
    private val LOGGER = LogManager.getFormatterLogger(MOD_ID)
    private const val MOD_NAME = "ParadoxConfig"

    @Suppress("unused")
    override fun onPreLaunch() {
        for (mod in FabricLoader.getInstance().allMods) {
            if (mod.metadata.containsCustomValue(MOD_ID)) {
                val obj = mod.metadata.getCustomValue(MOD_ID).asObject

                val rootPackage = obj.get("package").asString
                val classNames = obj.get("configs").asArray.map { it.asString }
                if (mod.metadata.id == MOD_ID && !FabricLoader.getInstance().isDevelopmentEnvironment) continue

                ConfigManager.initConfig(NewConfigData(rootPackage, classNames, mod.metadata.id))
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

