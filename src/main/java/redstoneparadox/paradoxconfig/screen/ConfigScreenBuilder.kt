package redstoneparadox.paradoxconfig.screen

import net.minecraft.client.gui.screen.Screen

interface ConfigScreenBuilder {

    /**
     * Called to create a new category in the screen.
     *
     * @param name The name of the category.
     */
    fun createCategory(name: String)

    /**
     * Called to complete a category.
     */
    fun finishCategory()

    /**
     * Called to add an option to the current category.
     *
     */
    fun addOption(name: String, getter: () -> Any,setter: (Any) -> Unit)

    fun addSaveFunction(saver: () -> Unit)

    /**
     * Builds the config screen.
     */
    fun build(): Screen
}