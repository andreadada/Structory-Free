package me.mrbast.structory.crafting.event;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.recipe.RecipeSlot;
import me.mrbast.structory.event.PlayerInteractStructureEvent;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;

public class CraftingInteractStructureEvent extends PlayerInteractStructureEvent {

    private Crafting crafting;

    public CraftingInteractStructureEvent(StructureInstance instance, Player player, Crafting crafting) {
        super(instance, player);
        this.crafting = crafting;
    }


    public Crafting getCrafting() {
        return crafting;
    }

    public void setCrafting(Crafting crafting) {
        this.crafting = crafting;
    }
}
