package redstoneparadox.paradoxconfig.conditions

import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import io.github.cottonmc.libcd.condition.ConditionalData
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import redstoneparadox.paradoxconfig.CONFIGS
import redstoneparadox.paradoxconfig.MODID

/**
 * Created by RedstoneParadox on 11/9/2019.
 */
fun registerConditions() {
    ConditionalData.registerCondition(Identifier(MODID, "option")) {

        if (it is JsonObject) {
            val configID = (it["config"] as? JsonPrimitive)?.value as? String
            
            
            if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "${MODID}:test.json5") {
                return@registerCondition false
            }
            else if (configID != null) {
                val config = CONFIGS[configID]
                val value = (it["value"] as? JsonPrimitive)?.value
                val option = (it["option"] as? JsonPrimitive)?.value as? String

                if (config != null && value != null && option != null) {
                    return@registerCondition config[option] == value
                }
            }
        }

        return@registerCondition false
    }
}