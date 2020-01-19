package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import redstoneparadox.paradoxconfig.conditions.registerConditions
import redstoneparadox.paradoxconfig.config.RootConfigCategory
import redstoneparadox.paradoxconfig.util.NewConfigData
import java.io.File
import java.io.FileNotFoundException

object ParadoxConfig: PreLaunchEntrypoint {
    const val MODID: String = "pconfig"
    internal var initialized: Boolean = false
    internal val CONFIGS: HashMap<String, RootConfigCategory> = hashMapOf()
    private val LOGGER = LogManager.getFormatterLogger(MODID)
    private const val MOD_NAME = "ParadoxConfig"

    @Suppress("unused")
    override fun onPreLaunch() {
        if (!initialized) {
            for (mod in FabricLoader.getInstance().allMods) {
                if (mod.metadata.containsCustomValue(MODID)) {
                    val obj = mod.metadata.getCustomValue(MODID).asObject

                    val rootPackage = obj.get("package").asString
                    val classNames = obj.get("configs").asArray.map { it.asString }
                    if (mod.metadata.id == MODID && !FabricLoader.getInstance().isDevelopmentEnvironment) continue

                    ConfigManager.initConfig(NewConfigData(rootPackage, classNames, mod.metadata.id))
                }
            }
        }
        initialized = true

        if (FabricLoader.getInstance().isModLoaded("libcd")) {
            registerConditions()
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

