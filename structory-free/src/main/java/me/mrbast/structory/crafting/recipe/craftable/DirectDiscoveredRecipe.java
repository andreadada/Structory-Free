package me.mrbast.structory.crafting.recipe.craftable;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.recipe.Recipe;
import me.mrbast.structory.manager.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectDiscoveredRecipe implements DiscoveredRecipe {


    private List<String> recipeKey;

    public DirectDiscoveredRecipe(String... key) {
        this.recipeKey = new ArrayList<>();
        Collections.addAll(this.recipeKey, key);
    }
    public DirectDiscoveredRecipe(List<String> keys) {
        this.recipeKey = new ArrayList<>();
        this.recipeKey.addAll(keys);
    }

    public DirectDiscoveredRecipe(){
        this.recipeKey = new ArrayList<>();
    }

    @Override
    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        recipeKey.forEach(key-> RecipeManager.getInstance().getRecipe(key).ifPresent(recipes::add));

        return recipes;
    }

    @Override
    public String saveString() {
        StringBuilder builder = new StringBuilder();

        builder.append("D:");
        recipeKey.forEach(key -> builder.append(key).append(";"));
        builder.delete(builder.length() - ";".length(), builder.length());

        return builder.toString();
    }

    @Override
    public void discover(Crafting crafting) {
        recipeKey.forEach(key-> RecipeManager.getInstance().getRecipe(key).ifPresent(crafting::discoverRecipe));
    }
}
