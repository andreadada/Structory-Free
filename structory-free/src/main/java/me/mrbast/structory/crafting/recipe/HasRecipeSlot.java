package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.event.PlayerCraftStructureEvent;
import me.mrbast.structory.structure.StructureInstance;
public interface HasRecipeSlot {

    boolean check(StructureInstance instance, RecipeSlot recipe);
}