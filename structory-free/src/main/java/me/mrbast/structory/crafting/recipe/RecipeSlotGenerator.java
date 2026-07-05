package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.structure.StructureInstance;

import java.util.Map;

public interface RecipeSlotGenerator {



    Map<Integer, RecipeSlot> generate(StructureInstance instance);

}
