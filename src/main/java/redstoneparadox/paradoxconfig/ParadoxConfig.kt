package redstoneparadox.paradoxconfig

import net.fabricmc.loader.api.FabricLoader
import redstoneparadox.paradoxconfig.misc.TestConfig
import redstoneparadox.paradoxconfig.misc.runTests
import redstoneparadox.paradoxconfig.serialization.ConfigDeserializer
import redstoneparadox.paradoxconfig.serialization.ConfigSerializer

@Suppress("unused")
fun init() {
    if (FabricLoader.getInstance().isDevelopmentEnvironment) {
        runTests()
    }
}