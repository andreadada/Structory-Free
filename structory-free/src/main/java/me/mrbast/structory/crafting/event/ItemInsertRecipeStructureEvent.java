package me.mrbast.structory.crafting.event;

import me.mrbast.structory.event.PlayerInteractStructureEvent;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemInsertRecipeStructureEvent extends PlayerInteractStructureEvent {

    private ItemStack itemStack;

    public ItemInsertRecipeStructureEvent(StructureInstance instance, Player player, ItemStack itemStack) {
        super(instance, player);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
