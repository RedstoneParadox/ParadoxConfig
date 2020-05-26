package io.github.redstoneparadox.paradoxconfig.screen

import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import net.minecraft.client.gui.screen.Screen

abstract class ConfigScreenBuilder {

    fun createScreen(modid: String): Screen {
        TODO("Unimplemented!")
    }

    abstract fun createConfigScreen(configs: List<ConfigCategory>): Screen
}