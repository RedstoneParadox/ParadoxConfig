package redstoneparadox.paradoxconfig.config

open class ConfigCategory(val key : String = "") {

    private val optionsMap: HashMap<String, ConfigOption<*>> = HashMap()
    private val categoriesMap: HashMap<String, ConfigCategory> = HashMap()

    internal fun init() {
        val kclass = this::class

        println("Being initialized!")

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
        optionsMap[key] = option
        return option
    }

    protected fun option(default: String, key: String, comment: String = ""): ConfigOption<String> {
        val option = ConfigOption(stringType, default, key, comment);
        optionsMap[key] = option
        return option
    }

    protected fun option(default: Short, key: String, comment: String = ""): ConfigOption<Short> {
        val option = ConfigOption(shortType, default, key, comment);
        optionsMap[key] = option
        return option
    }

    protected fun option(default: Int, key: String, comment: String = ""): ConfigOption<Int> {
        val option = ConfigOption(intType, default, key, comment)
        optionsMap[key] = option
        return option
    }

    protected fun option(default: Long, key: String, comment: String = ""): ConfigOption<Long> {
        val option = ConfigOption(longType, default, key, comment)
        optionsMap[key] = option
        return option
    }

    protected fun option(default: Double, key: String, comment: String = ""): ConfigOption<Double> {
        val option = ConfigOption(doubleType, default, key, comment)
        optionsMap[key] = option
        return option
    }

    protected fun rangedOption(default: Int, range: IntRange, key: String, comment: String): RangeConfigOption<Int> {
        val option = RangeConfigOption(intType, default, key, comment, range)
        optionsMap[key] = option
        return option
    }

    protected fun rangedOption(default: Long, range: LongRange, key: String, comment: String): RangeConfigOption<Long> {
        val option = RangeConfigOption(longType, default, key, comment, range)
        optionsMap[key] = option
        return option
    }

    private companion object {
        val boolType = Boolean::class
        val stringType = String::class
        val shortType = Short::class
        val intType = Int::class
        val longType = Long::class
        val doubleType = Double::class
    }
}