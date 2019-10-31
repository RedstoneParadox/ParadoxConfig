package redstoneparadox.paradoxconfig.config

import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer

open class ConfigCategory(val key : String = "", val comment: String = "") {

    private val optionsMap: HashMap<String, ConfigOption<*>> = HashMap()
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

    internal fun serialize(configSerializer: ConfigSerializer) {
        if (key.isNotEmpty()) configSerializer.addCategory(key, comment)

        for (category in categoriesMap.values) {
            category.serialize(configSerializer)
        }

        for (option in optionsMap.values) {
            option.serialize(configSerializer)
        }

        if (key.isNotEmpty()) configSerializer.exitCategory()
    }

    internal fun deserialize(configDeserializer: ConfigDeserializer) {
        if (key.isNotEmpty()) configDeserializer.enterCategory(key)

        for (category in categoriesMap.values) {
            category.deserialize(configDeserializer)
        }

        for (option in optionsMap.values) {
            option.deserialize(configDeserializer)
        }

        if (key.isNotEmpty()) configDeserializer.exitCategory()
    }

    protected fun option(default: Boolean, key: String, comment: String = ""): ConfigOption<Boolean> {
        val option = ConfigOption(boolType, default, key, "$comment [Values: true/false]")
        optionsMap[key] = option
        return option
    }

    protected fun option(default: String, key: String, comment: String = ""): ConfigOption<String> {
        val option = ConfigOption(stringType, default, key, "$comment [Values: any string]")
        optionsMap[key] = option
        return option
    }

    protected fun option(default: Long, key: String, comment: String = ""): ConfigOption<Long> {
        val option = ConfigOption(longType, default, key, "$comment [Values: any whole number]")
        optionsMap[key] = option
        return option
    }

    protected fun option(default: Double, key: String, comment: String = ""): ConfigOption<Double> {
        val option = ConfigOption(doubleType, default, key, "$comment [Values: any decimal number]")
        optionsMap[key] = option
        return option
    }

    protected fun rangedOption(default: Long, range: LongRange, key: String, comment: String = ""): RangeConfigOption<Long> {
        val option = RangeConfigOption(longType, default, key, "$comment [Values: ${range.first} to ${range.last}]", range)
        optionsMap[key] = option
        return option
    }

    private companion object {
        val boolType = Boolean::class
        val stringType = String::class
        val longType = Long::class
        val doubleType = Double::class
    }
}