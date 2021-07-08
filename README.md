# Paradox Config

A lightweight, Kotlin-based config API for Minecraft.

Discord: https://discord.gg/crZpcjtdJR

## Adding to your project:

build.gradle:
```gradle
repositories {
	//...
	maven { url 'https://jitpack.io' }
}

dependencies {
 	// ...

	modApi("com.github.RedstoneParadox:ParadoxConfig:<version>") {
		exclude group: 'net.fabricmc.fabric-api'
		exclude group: 'net.fabricmc.fabric-language-kotlin'
	}
	include "com.github.RedstoneParadox:ParadoxConfig:<version>"
}
```

## Adding versions prior to 0.5.0 Beta to your project:

You probably can't. Sorry.
