package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.manager.RecipeManager;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class NamespacedKeyRecipeGroup implements RecipeGroup{

    private List<NamespacedKey> recipesName;
    private String key;

    public NamespacedKeyRecipeGroup(String key) {
        this.recipesName = new ArrayList<>();
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public List<Recipe> getRecipes() {
        RecipeManager recipeManager = RecipeManager.getInstance();
        List<Recipe> recipes = new ArrayList<>();
        recipesName.forEach(x->recipeManager.getRecipe(x).ifPresent(recipes::add));
        return recipes;
    }

    @Override
    public void addRecipe(Recipe recipe) {
        this.recipesName.add(recipe.getKey());
    }
}
