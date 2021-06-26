package io.github.redstoneparadox.paradoxconfig.screen

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.TranslatableText

class ClothConfigBuilder(private val modid: String) {
    fun createScreen(config: ConfigCategory, parent: Screen) {
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(TranslatableText("paradoxconfig.$modid.title"))
    }

    fun createCategory(builder: ClothConfigBuilder, category: ConfigCategory) {
        
    }
}