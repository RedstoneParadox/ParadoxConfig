package io.github.redstoneparadox.paradoxconfig.conditions

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.github.cottonmc.libcd.api.condition.ConditionManager
import io.github.cottonmc.libcd.api.init.ConditionInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import io.github.redstoneparadox.paradoxconfig.ParadoxConfig.CONFIGS
import io.github.redstoneparadox.paradoxconfig.ParadoxConfig.MODID
import io.github.redstoneparadox.paradoxconfig.util.ReflectionUtil
import io.github.redstoneparadox.paradoxconfig.util.compareTo

/**
 * Created by RedstoneParadox on 11/9/2019.
 */
object ParadoxConfigCD: ConditionInitializer {
    override fun initConditions(manager: ConditionManager) {
        manager.registerCondition(Identifier(MODID, "option")) {

            if (it is JsonObject) {
                val configID = (it["config"] as? JsonPrimitive)?.asString

                if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "${MODID}:test.json5") {
                    return@registerCondition false
                }
                else if (configID != null) {
                    val config = CONFIGS[configID]
                    val primitive = (it["value"] as? JsonPrimitive)
                    val predicate = (it["predicate"] as? JsonPrimitive)?.asString
                    val option = (it["option"] as? JsonPrimitive)?.asString

                    if (config != null && primitive != null && option != null) {
                        val op = config[option]
                        if (predicate != null) {
                            return@registerCondition when (predicate) {
                                "==" -> op == ReflectionUtil.getPrimitiveValue(primitive)
                                "!=" -> op != ReflectionUtil.getPrimitiveValue(primitive)
                                "<" -> if (op is Number) op < primitive.asNumber else false
                                ">" -> if (op is Number) op > primitive.asNumber else false
                                "<=" -> if (op is Number) op <= primitive.asNumber else false
                                ">=" -> if (op is Number) op >= primitive.asNumber else false
                                else -> false
                            }
                        }
                        else {
                            return@registerCondition op == ReflectionUtil.getPrimitiveValue(primitive)
                        }
                    }
                }
            }

            return@registerCondition false
        }

        manager.registerCondition(Identifier(MODID, "contains")) {
            if (it is JsonObject) {
                val configID = (it["config"] as? JsonPrimitive)?.asString


                if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "${MODID}:test.json5") {
                    return@registerCondition false
                } else if (configID != null) {
                    val config = CONFIGS[configID]
                    val contains = (it["contains"] as? JsonArray)?.toList()
                    val option = (it["option"] as? JsonPrimitive)?.asString

                    if (config != null && contains != null && option != null) {
                        val optionCollection = config[option] as? Collection<out Any>

                        if (optionCollection != null && optionCollection.size >= contains.size) {
                            for (element in contains) {
                                if (!(element is JsonPrimitive && optionCollection.contains(ReflectionUtil.getPrimitiveValue(element)))) {
                                    return@registerCondition false
                                }

                            }
                            return@registerCondition true
                        }
                    }
                }
            }

            return@registerCondition false
        }
    }

}