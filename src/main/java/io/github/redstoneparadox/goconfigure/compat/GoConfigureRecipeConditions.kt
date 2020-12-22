package io.github.redstoneparadox.goconfigure.compat

import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import io.github.redstoneparadox.goconfigure.ConfigManager
import io.github.redstoneparadox.goconfigure.GoConfigure
import io.github.redstoneparadox.goconfigure.util.ReflectionUtil
import io.github.redstoneparadox.goconfigure.util.compareTo
import io.github.ytg1234.recipeconditions.api.RecipeConds
import io.github.ytg1234.recipeconditions.api.condition.base.RecipeCondition
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

@Suppress("unused")
object GoConfigureRecipeConditions: ModInitializer {
    override fun onInitialize() {
        if (!FabricLoader.getInstance().isModLoaded("recipeconditions")) return

        Registry.register(RecipeConds.RECIPE_CONDITION, Identifier(GoConfigure.MOD_ID, "option"), RecipeCondition { param ->
            val paramObject = param.`object`()
            val configID = (paramObject["config"] as? JsonPrimitive)?.asString

            if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "${GoConfigure.MOD_ID}:test.json5") {
                return@RecipeCondition false
            }
            else if (configID != null) {
                val config = ConfigManager.getConfig(configID)
                val primitive = (paramObject["value"] as? JsonPrimitive)
                val predicate = (paramObject["predicate"] as? JsonPrimitive)?.asString
                val option = (paramObject["option"] as? JsonPrimitive)?.asString

                if (config != null && primitive != null && option != null) {
                    val op = config[option]
                    if (predicate != null) {
                        return@RecipeCondition when (predicate) {
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
                        return@RecipeCondition op == ReflectionUtil.getPrimitiveValue(primitive)
                    }
                }
            }

            return@RecipeCondition false
        })

        Registry.register(RecipeConds.RECIPE_CONDITION, Identifier(GoConfigure.MOD_ID, "contains"), RecipeCondition { param ->
            val paramObject = param.`object`()
            val configID = (paramObject["config"] as? JsonPrimitive)?.asString

            if (!FabricLoader.getInstance().isDevelopmentEnvironment && configID == "${GoConfigure.MOD_ID}:test.json5") {
                return@RecipeCondition false
            } else if (configID != null) {
                val config = ConfigManager.getConfig(configID)
                val contains = (paramObject["contains"] as? JsonArray)?.toList()
                val option = (paramObject["option"] as? JsonPrimitive)?.asString

                if (config != null && contains != null && option != null) {
                    val optionCollection = config[option] as? Collection<out Any>

                    if (optionCollection != null && optionCollection.size >= contains.size) {
                        for (element in contains) {
                            if (!(element is JsonPrimitive && optionCollection.contains(_root_ide_package_.io.github.redstoneparadox.goconfigure.util.ReflectionUtil.getPrimitiveValue(element)))) {
                                return@RecipeCondition false
                            }

                        }
                        return@RecipeCondition true
                    }
                }
            }

            return@RecipeCondition false
        })
    }
}