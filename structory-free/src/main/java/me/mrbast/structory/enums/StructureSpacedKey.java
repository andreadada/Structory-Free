package me.mrbast.structory.enums;

import me.mrbast.structory.Structory;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public enum StructureSpacedKey implements Key{


    INGREDIENT_CUSTOM_ITEM_ACCEPTOR("saveditem"),
    INGREDIENT_ITEM_ACCEPTOR("item"),
    FIREWORK_CANCEL_DMG("firework_cancel_dmg"),
    RECIPE_TEST("recipe_test"),
    PARTICLE_SIZEDBOX("boxsized"),
    PARTICLE_FLAME("flame"),
    OPTION_NOTIFY("notify"),
    OPTION_CRAFTING("crafting"),
    OPTION_FIREWORK("fireworks"),
    OPTION_PARTICLE("particle"),
    RECIPE_SLOT_ITEM("recipe_slot_item"), RECIPE_SLOT("recipe_slot"), RECIPE_SLOT_ID("recipe_slot_id"), OPTION_INTERACT("interact");

    private final NamespacedKey namespacedKey;
    private static final Plugin plugin = Structory.getPlugin(Structory.class);
    StructureSpacedKey(String name) {
        this.namespacedKey = new NamespacedKey(Structory.getPlugin(Structory.class), name);
    }


    @Override
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
