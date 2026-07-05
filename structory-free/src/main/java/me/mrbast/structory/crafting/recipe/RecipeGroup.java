package me.mrbast.structory.crafting.recipe;

import java.util.List;

public interface RecipeGroup {


    String key();
    List<Recipe> getRecipes();

    void addRecipe(Recipe recipe);
}
