package me.mrbast.structory.crafting.recipe.craftable;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.recipe.Recipe;

import java.util.List;

public interface DiscoveredRecipe {




    List<Recipe> getRecipes();

    String saveString();

    void discover(Crafting crafting);
}
