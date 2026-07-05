package me.mrbast.structory.enums;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public interface Key {

    NamespacedKey getNamespacedKey();
    Plugin getPlugin();
}
