# paradox-config

[ ![Download](https://api.bintray.com/packages/redstoneparadox/mods/paradox-config/images/download.svg?version=v0.3.2-alpha) ](https://bintray.com/redstoneparadox/mods/paradox-config/v0.3.2-alpha/link)

A lightweight, Kotlin-based config API for Minecraft.


## Adding to your project:

build.gradle:
```gradle
repositories {
	maven {
		url = "https://dl.bintray.com/redstoneparadox/mods"
	}
}

dependencies {
  // ...

	modApi("redstoneparadox:paradox-config:VERSION") {
		exclude group: 'net.fabricmc.fabric-api'
		exclude group: 'net.fabricmc.fabric-language-kotlin'
	}
	include "redstoneparadox:paradox-config:VERSION"
}
```
