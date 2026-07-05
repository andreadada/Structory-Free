package me.mrbast.structory.crafting.recipe.result;

import me.mrbast.structory.crafting.recipe.RecipeContext;
import me.mrbast.structory.structure.StructureInstance;

public abstract class Result {

    public abstract void craft(RecipeContext crafting);
}
