package me.mrbast.structory.crafting.recipe.ingredient;


import me.mrbast.structory.crafting.recipe.Recipe;
import me.mrbast.structory.crafting.recipe.RecipeContext;
import me.mrbast.structory.event.PlayerCraftStructureEvent;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Ingredient{


    public boolean consume = true;

    public abstract boolean check(RecipeContext context);
    public abstract void consume(RecipeContext context);
    public abstract NamespacedKey getKey();


    public void setConsume(boolean consume) {
        this.consume = consume;
    }

    public boolean doConsume() {
        return consume;
    }
}