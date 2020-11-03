# paradox-config

[ ![Download](https://api.bintray.com/packages/redstoneparadox/mods/paradox-config/images/download.svg?version=0.3.6-alpha) ](https://bintray.com/redstoneparadox/mods/paradox-config/0.3.6-alpha/link)

A lightweight, Kotlin-based config API for Minecraft.

Discord: https://discord.gg/xSZHvCc

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

	modApi("io.github.redstoneparadox:paradox-config:VERSION") {
		exclude group: 'net.fabricmc.fabric-api'
		exclude group: 'net.fabricmc.fabric-language-kotlin'
	}
	include "io.github.redstoneparadox:paradox-config:VERSION"
}
```
