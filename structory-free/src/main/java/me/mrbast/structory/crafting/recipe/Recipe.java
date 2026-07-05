package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.recipe.ingredient.ItemIngredient;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public interface Recipe {


    NamespacedKey getKey();
    void setKey(NamespacedKey key);
    boolean accept(RecipeContext recipeContext);
    boolean isAutomaticallyDiscovered();
    void addRecipeGroup(RecipeGroup group);
    boolean hasDeterministic();
    List<RecipeGroup> getRecipeGroups();
    void consume(RecipeContext recipeContext);
    void result(RecipeContext recipeContext);

    boolean hasDisplayName();
    String getDisplayName();
    void setDisplayName(String displayName);

    Set<DeterministicItem> getDeterministicItems();
}
