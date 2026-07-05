package me.mrbast.structory.util;

import org.bukkit.Color;

import java.lang.reflect.Field;

public class ColorUtil {





    public static Color getColor(String clr) {
        Color color = null;
        try {
            Field field = Color.class.getField(clr);
            field.setAccessible(true);
            color = (Color) field.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return color;

    }


}
