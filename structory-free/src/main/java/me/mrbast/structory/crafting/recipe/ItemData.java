package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.crafting.Crafting;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemData {

    private UUID uuid;
    private Crafting crafting;
    private RecipeSlot recipeSlot;
    private ItemStack item;

    public ItemData(RecipeSlot recipeSlot, UUID uuid) {
        this.uuid = uuid;
        this.recipeSlot = recipeSlot;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Crafting getCrafting() {
        return crafting;
    }

    public void setCrafting(Crafting crafting) {
        this.crafting = crafting;
    }

    public RecipeSlot getRecipeSlot() {
        return recipeSlot;
    }

    public void setRecipeSlot(RecipeSlot recipeSlot) {
        this.recipeSlot = recipeSlot;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
