package io.github.redstoneparadox.goconfigure.compat

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.github.cottonmc.libcd.api.LibCDInitializer
import io.github.cottonmc.libcd.api.condition.ConditionManager
import io.github.goconfigure.paradoxconfig.ConfigManager
import io.github.goconfigure.paradoxconfig.GoConfigure.MOD_ID
import io.github.redstoneparadox.goconfigure.util.ReflectionUtil
import io.github.goconfigure.paradoxconfig.util.compareTo
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier

/**
 * Created by RedstoneParadox on 11/9/2019.
 */
object GoConfigureLibCD: LibCDInitializer {
    override fun initConditions(manager: ConditionManager) {
        manager.registerCondition(Identifier(MOD_ID, "option")) {

            if (it is JsonObject) {
                val configID = (it["config"] as? JsonPrimitive)?.asString

                if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "${MOD_ID}:test.json5") {
                    return@registerCondition false
                }
                else if (configID != null) {
                    val config = ConfigManager.getConfig(configID)
                    val primitive = (it["value"] as? JsonPrimitive)
                    val predicate = (it["predicate"] as? JsonPrimitive)?.asString
                    val option = (it["option"] as? JsonPrimitive)?.asString

                    if (config != null && primitive != null && option != null) {
                        val op = config[option]
                        if (predicate != null) {
                            return@registerCondition when (predicate) {
                                "==" -> op == _root_ide_package_.io.github.redstoneparadox.goconfigure.util.ReflectionUtil.getPrimitiveValue(primitive)
                                "!=" -> op != _root_ide_package_.io.github.redstoneparadox.goconfigure.util.ReflectionUtil.getPrimitiveValue(primitive)
                                "<" -> if (op is Number) op < primitive.asNumber else false
                                ">" -> if (op is Number) op > primitive.asNumber else false
                                "<=" -> if (op is Number) op <= primitive.asNumber else false
                                ">=" -> if (op is Number) op >= primitive.asNumber else false
                                else -> false
                            }
                        }
                        else {
                            return@registerCondition op == _root_ide_package_.io.github.redstoneparadox.goconfigure.util.ReflectionUtil.getPrimitiveValue(primitive)
                        }
                    }
                }
            }

            return@registerCondition false
        }

        manager.registerCondition(Identifier(MOD_ID, "contains")) {
            if (it is JsonObject) {
                val configID = (it["config"] as? JsonPrimitive)?.asString


                if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "$MOD_ID:test.json5") {
                    return@registerCondition false
                } else if (configID != null) {
                    val config = ConfigManager.getConfig(configID)
                    val contains = (it["contains"] as? JsonArray)?.toList()
                    val option = (it["option"] as? JsonPrimitive)?.asString

                    if (config != null && contains != null && option != null) {
                        val optionCollection = config[option] as? Collection<out Any>

                        if (optionCollection != null && optionCollection.size >= contains.size) {
                            for (element in contains) {
                                if (!(element is JsonPrimitive && optionCollection.contains(_root_ide_package_.io.github.redstoneparadox.goconfigure.util.ReflectionUtil.getPrimitiveValue(element)))) {
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