package io.github.redstoneparadox.paradoxconfig.util;

import org.jetbrains.annotations.Nullable;

public class ReflectionUtil {

    // Doing this from Kotlin code causes issues.
    public static @Nullable Class<?> getClassForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
