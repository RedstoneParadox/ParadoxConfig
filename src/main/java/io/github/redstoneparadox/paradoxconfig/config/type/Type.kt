package io.github.redstoneparadox.paradoxconfig.config.type

import net.minecraft.util.Identifier

abstract class Type<T, U> {
    abstract fun fromU(u: U?): T

    abstract fun intoU(t: T?): U

    object StringType: Type<String, String>() {
        override fun fromU(u: String?): String {
            return if (u is String) {u} else {""}
        }

        override fun intoU(t: String?): String {
            return t ?: ""
        }
    }

    object IdentifierType: Type<Identifier, String>() {
        override fun fromU(u: String?): Identifier {
            return if (u != null) { Identifier(u) } else { Identifier("missingno") }
        }

        override fun intoU(t: Identifier?): String {
            return t?.toString() ?: "minecraft:missingno"
        }
    }

    class EnumType<E: Enum<E>>(private val values: Array<E>): Type<E, String>() {
        override fun fromU(u: String?): E {
            if (u == null) return values[0]
            for (value in values) {
                if (value.name == u) return value
            }
            return values[0]
        }

        override fun intoU(t: E?): String {
            if (t != null) return t.name
            return values[0].name
        }

        inline fun <reified E: Enum<E>> create(): EnumType<E> {
            return EnumType(enumValues())
        }
    }
}