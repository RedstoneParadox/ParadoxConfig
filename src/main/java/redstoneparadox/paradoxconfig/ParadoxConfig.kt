package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.config.ConfigCategory
import redstoneparadox.paradoxconfig.misc.runTests

@Suppress("unused")
fun init() {
    for (path in getConfigClassNames()) {
        println(path)
    }

    if (FabricLoader.getInstance().isDevelopmentEnvironment) {
        runTests()
    }
}

fun getConfigClassNames(): ArrayList<String> {
    val classpaths = arrayListOf<String>()

    for (mod in FabricLoader.getInstance().allMods) {
        val cv = mod.metadata.getCustomValue("paradoxconfig")
        if (cv != null) {
            val cvArray = cv.asArray
            for (stringCV in cvArray) {
                classpaths.add(stringCV.asString)
            }
        }
    }

    return classpaths
}

fun initConfigs() {
    val classNames = getConfigClassNames()
    val categoryClass = ConfigCategory::class

    for (name in classNames) {
        val configInstance = Class.forName(name).kotlin.objectInstance

        if (configInstance is ConfigCategory) {
            configInstance.init()
        }
        else {
            println("Object $name either doesn't extend ${categoryClass.simpleName} or is not an object.")
        }
    }
}

