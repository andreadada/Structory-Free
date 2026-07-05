package me.mrbast.structory.crafting.recipe.ingredient;

import me.mrbast.structory.crafting.recipe.DeterministicItem;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.manager.SavedItemManager;
import me.mrbast.structory.saveditem.SavedItemProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemIngredient extends ItemIngredient {


    private String itemKey;

    public CustomItemIngredient(){
        setHasDeterministic(true);
    }

    private boolean isSimilar(ItemStack one, ItemStack stack) {
        if (stack == null) {
            return false;
        } else if (stack == one) {
            return true;
        } else {
            Material comparisonType = one.getType().isLegacy() ? Bukkit.getUnsafe().fromLegacy(one.getData(), true) : one.getType();
            return comparisonType == stack.getType() && one.getDurability() == stack.getDurability() && one.hasItemMeta() == stack.hasItemMeta() && (!one.hasItemMeta() || Bukkit.getItemFactory().equals(one.getItemMeta(), stack.getItemMeta()));
        }
    }

    public CustomItemIngredient key(String key) {

        this.itemKey = key;

        SavedItemProvider val = SavedItemManager.getInstance().get(key);
        if(val == null) return null;

        filters.add(x-> val.getItem().isSimilar(x));

        val.registerUpdate(this);
        update();
        return this;
    }

    public void update(){
        SavedItemProvider val = SavedItemManager.getInstance().get(itemKey);
        if(val == null) {
            return;
        }

        ItemStack item = val.getItem().clone();
        ItemMeta meta = item.getItemMeta();
        String title = null;
        int modelData = 0;
        if(meta != null) {
            if(meta.hasDisplayName()) title = meta.getDisplayName();
            if(meta.hasCustomModelData()) modelData = meta.getCustomModelData();
        }
        setDeterministicItem(new DeterministicItem(item.getType(), title, modelData, item.getAmount()));
    }

    @Override
    public DeterministicItem getDeterministicItem() {
        return super.getDeterministicItem();
    }

    @Override
    public NamespacedKey getKey() {
        return StructureSpacedKey.INGREDIENT_CUSTOM_ITEM_ACCEPTOR.getNamespacedKey();
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
}
