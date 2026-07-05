package me.mrbast.structory.crafting.recipe.craftable;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.recipe.Recipe;
import me.mrbast.structory.manager.RecipeManager;

import java.util.ArrayList;
import java.util.List;

public class GroupDiscoveredRecipe implements DiscoveredRecipe{


    private String groupKey;

    public GroupDiscoveredRecipe(String groupKey) {
        this.groupKey = groupKey;
    }


    @Override
    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        RecipeManager.getInstance().getRecipeGroup(groupKey).ifPresent(recipeGroup -> recipes.addAll(recipeGroup.getRecipes()));

        return recipes;
    }

    @Override
    public String saveString() {
        return "G:" + groupKey;
    }

    @Override
    public void discover(Crafting crafting) {
        RecipeManager.getInstance().getRecipeGroup(groupKey).ifPresent(crafting::discoverRecipe);
    }
}
