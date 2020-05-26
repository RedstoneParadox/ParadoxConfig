package io.github.redstoneparadox.paradoxconfig.screen

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.*
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.LiteralText
import java.util.function.Consumer

class LibGuiConfigScreenBuilder: ConfigScreenBuilder() {
    override fun createConfigScreen(configs: List<ConfigCategory>): Screen {

        return LibGUIConfigScreen(ConfigGui())
    }



    private fun createBoolOption(name: String, setter: (Boolean) -> Boolean, default: Boolean): WWidget {
        val panel = WPlainPanel()
        panel.add(WText(LiteralText(name)), 0, 0, 20, 10)

        val button = WToggleButton(LiteralText(default.toString()))
        button.onToggle = Consumer {
            button.toggle = setter(it)
        }
        button.toggle = default

        panel.add(button, 24, 0, 20, 10)

        return panel
    }

    private fun createTextFieldOption(name: String, setter: (String) -> String, default: String): WWidget {
        val panel = WPlainPanel()
        panel.add(WText(LiteralText(name)), 0, 0, 20, 10)

        val textField = WTextField()
        textField.text = default
        textField.setChangedListener {
            textField.text = setter(it)
        }

        return panel
    }

    class LibGUIConfigScreen(description: GuiDescription) : CottonClientScreen(description) {

    }

    class ConfigGui(): LightweightGuiDescription() {

    }
}