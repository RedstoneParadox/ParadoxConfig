package io.github.redstoneparadox.paradoxconfig.util;

import blue.endless.jankson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

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

    public static @Nullable Object getPrimitiveValue(JsonPrimitive jsonPrimitive) {
        try {
            Field valueField = jsonPrimitive.getClass().getDeclaredField("value");
            valueField.setAccessible(true);
            return valueField.get(jsonPrimitive);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println("Unable to access field for config reasons; please report to the Paradox Config issue tracker.");
            e.printStackTrace();
        }

        return null;
    }
}
