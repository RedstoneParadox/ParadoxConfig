package redstoneparadox.paradoxconfig.conditions

import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import io.github.cottonmc.libcd.condition.ConditionalData
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import redstoneparadox.paradoxconfig.CONFIGS
import redstoneparadox.paradoxconfig.MODID
import redstoneparadox.paradoxconfig.util.compareTo

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
                val predicate = (it["predicate"] as? JsonPrimitive)?.value as? String
                val option = (it["option"] as? JsonPrimitive)?.value as? String

                if (config != null && value != null && option != null) {
                    val op = config[option]
                    if (predicate != null) {
                        return@registerCondition when (predicate) {
                            "==" -> op == value
                            "!=" -> op != value
                            "<" -> if (op is Number && value is Number) op < value else false
                            ">" -> if (op is Number && value is Number) op > value else false
                            "<=" -> if (op is Number && value is Number) op <= value else false
                            ">=" -> if (op is Number && value is Number) op >= value else false
                            else -> false
                        }
                    }
                    else {
                        return@registerCondition op == value
                    }
                }
            }
        }

        return@registerCondition false
    }
}