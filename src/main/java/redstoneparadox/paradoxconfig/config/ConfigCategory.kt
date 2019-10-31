package redstoneparadox.paradoxconfig.config

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

open class ConfigCategory(val key : String = "") {

    internal val optionsMap: HashMap<String, ConfigOption<*>> = HashMap()
    private val categoriesMap: HashMap<String, ConfigCategory> = HashMap()

    internal fun init() {
        val kclass = this::class

        for (innerclass in kclass.nestedClasses) {
            val obj = innerclass.objectInstance
            if (obj is ConfigCategory) {
                categoriesMap[obj.key] = obj
                obj.init()
            }
        }
    }

    protected fun option(default: Boolean, key: String, comment: String = ""): ConfigOption<Boolean> {
        val option = ConfigOption(boolType, default, key, comment);
        optionsMap[key] = option;
        return option
    }

    protected fun option(default: Double, key: String, comment: String = ""): ConfigOption<Double> {
        val option = ConfigOption(doubleType, default, key, comment)
        optionsMap[key] = option
        return option;
    }

    protected fun option(default: Long, key: String, comment: String = ""): ConfigOption<Long> {
        val option = ConfigOption(longType, default, key, comment)
        optionsMap[key] = option
        return option;
    }

    private companion object {
        val boolType = Boolean::class
        val doubleType = Double::class
        val longType = Long::class
    }
}