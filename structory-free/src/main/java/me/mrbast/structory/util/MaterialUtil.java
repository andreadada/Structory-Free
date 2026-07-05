package me.mrbast.structory.util;

import org.bukkit.Material;

public class MaterialUtil {


    public static Material parse(String str){
        if(str.startsWith("minecraft:")){
            str = str.substring("minecraft:".length());
            str = str.toUpperCase();
        }
        return Material.getMaterial(str);
    }

    public static boolean areSame(Material checkBlock, Material type) {
        if(checkBlock == null || type == null)  return false;
        return checkBlock == type;
    }
}
