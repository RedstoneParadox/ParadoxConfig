package io.github.redstoneparadox.paradoxconfig.screen

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.*
import io.github.redstoneparadox.paradoxconfig.config.ConfigCategory
import io.github.redstoneparadox.paradoxconfig.screen.widget.WCardPanel
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.LiteralText
import org.lwjgl.system.CallbackI
import java.util.function.Consumer

abstract class LibGuiConfigScreenBuilder: ConfigScreenBuilder() {
    override fun createConfigScreen(configs: List<ConfigCategory>): Screen {
        val cardPanel = WCardPanel()

        if (configs.size == 1) {
            makeCategoryPage(configs[0], cardPanel)
        }

        return LibGUIConfigScreen(ConfigGui(cardPanel))
    }


    private fun makeCategoryPage(category: ConfigCategory, cardPanel: WCardPanel, superPage: Int = -1): Int {
        val data = mutableListOf<Data>()
        val page = cardPanel.pageCount
        cardPanel.addCard(WPlainPanel())

        if (superPage >= 0) {
            data.add(Data.CategoryData(WButton().setOnClick { cardPanel.select(superPage) }))
        }

        for (subcategory in category.getSubcategories()) {
            val subPage = makeCategoryPage(subcategory, cardPanel, page)
            data.add(Data.CategoryData(WButton().setOnClick { cardPanel.select(subPage) }))
        }

        for (option in category.getOptions()) {
            if (option.getKClass() == Boolean::class) {
                data.add(createBoolOption(option.key, option.get() as Boolean) {
                    option.set(it); option.get() as Boolean
                })
            }
            else if (option.getKClass() == String::class) {
                data.add(createTextFieldOption(option.key, option.get() as String) {
                    option.set(it); option.get() as String
                })
            }
        }

        val list = WListPanel<Data, WPlainPanel>(data, ::WPlainPanel) { data, panel ->
            if (data is Data.OptionData) {
                panel.add(data.label, 0, 2, 20, 10)
                panel.add(data.widget, 24, 2, 20, 10)
            }
            else if (data is Data.CategoryData) {
                panel.add(data.button, 2, 2, 40, 10)
            }
        }
        list.setListItemHeight(24)
        cardPanel.setCard(page, list)
        return page
    }

    private fun createBoolOption(name: String, default: Boolean, setter: (Boolean) -> Boolean): Data {
        val button = WToggleButton(LiteralText(default.toString()))
        button.onToggle = Consumer {
            button.toggle = setter(it)
            button.label = LiteralText(it.toString())
        }
        button.toggle = default
        button.label = LiteralText(default.toString())

        return Data.OptionData(WText(LiteralText(name)), button)
    }

    private fun createTextFieldOption(name: String, default: String, setter: (String) -> String): Data {
        val textField = WTextField()
        textField.text = default
        textField.setChangedListener {
            textField.text = setter(it)
        }

        return Data.OptionData(WText(LiteralText(name)), textField)
    }

    sealed class Data {
        class OptionData(val label: WText, val widget: WWidget): Data()
        class CategoryData(val button: WButton): Data()
    }

    class LibGUIConfigScreen(description: GuiDescription) : CottonClientScreen(description) {

    }

    class ConfigGui(panel: WCardPanel): LightweightGuiDescription() {
        init {
            panel.validate(this)
        }
    }
}