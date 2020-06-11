# paradox-config

[ ![Download](https://api.bintray.com/packages/io.github.redstoneparadox/mods/paradox-config/images/download.svg?version=v0.3.3-alpha) ](https://bintray.com/io.github.redstoneparadox/mods/paradox-config/v0.3.3-alpha/link)

A lightweight, Kotlin-based config API for Minecraft.


## Adding to your project:

build.gradle:
```gradle
repositories {
	maven {
		url = "https://dl.bintray.com/io.github.redstoneparadox/mods"
	}
}

dependencies {
  // ...

	modApi("io.github.io.github.redstoneparadox:paradox-config:VERSION") {
		exclude group: 'net.fabricmc.fabric-api'
		exclude group: 'net.fabricmc.fabric-language-kotlin'
	}
	include "io.github.redstoneparadox:paradox-config:VERSION"
}
```

Discord: https://discord.gg/3kAN8vC
