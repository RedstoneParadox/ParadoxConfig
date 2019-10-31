package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.misc.runTests

@Suppress("unused")
fun init() {
    if (FabricLoader.getInstance().isDevelopmentEnvironment) {
        runTests()
    }

    for (path in getConfigClasses()) {
        println(path)
    }
}

fun getConfigClasses(): ArrayList<String> {
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