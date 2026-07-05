package me.mrbast.structory.crafting.event;

import me.mrbast.structory.crafting.recipe.RecipeSlot;
import me.mrbast.structory.event.PlayerInteractStructureEvent;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;

public class RecipeSlotPickUpItemStructureEvent extends PlayerInteractStructureEvent {

    private RecipeSlot recipeSlot;

    public RecipeSlotPickUpItemStructureEvent(StructureInstance instance, RecipeSlot recipeSlot, Player player) {
        super(instance, player);
        this.recipeSlot = recipeSlot;
    }

    public RecipeSlot getRecipeSlot() {
        return recipeSlot;
    }
}
