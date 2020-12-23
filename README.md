# Paradox Config

[ ![Download](https://api.bintray.com/packages/redstoneparadox/mods/ParadoxConfig/images/download.svg) ](https://bintray.com/redstoneparadox/mods/ParadoxConfig/_latestVersion)

A lightweight, Kotlin-based config API for Minecraft, formerly known as Paradox Config.

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

	modApi("io.github.redstoneparadox:paradox-config:<version>") {
		exclude group: 'net.fabricmc.fabric-api'
		exclude group: 'net.fabricmc.fabric-language-kotlin'
	}
	include "io.github.redstoneparadox:ParadoxConfig:<version>"
}
```

(For versions prior to 0.4.0-beta:)

build.gradle:
```gradle
repositories {
	maven {
		url = "https://dl.bintray.com/io.github.redstoneparadox/mods"
	}
}

dependencies {
  // ...

	modApi("io.github.redstoneparadox:paradox-config:<version>") {
		exclude group: 'net.fabricmc.fabric-api'
		exclude group: 'net.fabricmc.fabric-language-kotlin'
	}
	include "io.github.redstoneparadox:paradox-config:<version>"
}
```
