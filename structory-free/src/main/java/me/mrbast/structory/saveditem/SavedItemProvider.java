package me.mrbast.structory.saveditem;

import me.mrbast.structory.Structory;
import me.mrbast.structory.crafting.recipe.ingredient.CustomItemIngredient;
import me.mrbast.structory.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SavedItemProvider {


    private NamespacedKey key;
    private String base64;
    public ItemStack item;

    private final Set<CustomItemIngredient> ingredients = new HashSet<>();

    public SavedItemProvider(NamespacedKey key, String base64) {
        this.key = key;
        this.base64 = base64;
        try {
            this.item = ItemStackUtil.itemStackFromBase64(base64);
        } catch (Throwable e) {
            this.item = new ItemStack(Material.AIR);
            Structory.getPlugin(Structory.class).getLogger().severe("Error while loading item " + key.getKey() + ", replacing with AIR");
        }
    }

    public SavedItemProvider(NamespacedKey key, ItemStack stack) {
        this.key = key;
        this.item = stack.clone();
        try {
            this.base64 = ItemStackUtil.itemStackToBase64(stack);
        } catch (Exception e) {
            try {
                this.base64 = ItemStackUtil.itemStackToBase64(new ItemStack(Material.AIR));
            } catch (Exception ex) {
                Structory.getPlugin(Structory.class).getLogger().severe("Error while saving item " + key.getKey() + ", replacing with AIR");
            }
            Structory.getPlugin(Structory.class).getLogger().severe("Error while saving item " + key.getKey() + ", replacing with AIR");
        }
    }

    public void changeItem(ItemStack item) {
        this.item = item.clone();
        try {
            this.base64 = ItemStackUtil.itemStackToBase64(this.item);
        } catch (Exception e) {
            try {
                this.base64 = ItemStackUtil.itemStackToBase64(new ItemStack(Material.AIR));
            } catch (Exception ex) {
                Structory.getPlugin(Structory.class).getLogger().severe("Error while saving item " + key.getKey() + ", replacing with AIR");
            }
            Structory.getPlugin(Structory.class).getLogger().severe("Error while saving item " + key.getKey() + ", replacing with AIR");
        }

        ingredients.forEach(CustomItemIngredient::update);
    }

    public ItemStack build(){return item.clone();}


    public NamespacedKey getKey() {
        return key;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getBase64() {
        return base64;
    }

    public void setKey(NamespacedKey key) {
        this.key = key;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "SavedItemProvider{" +
                "key=" + key +
                ", base64='" + base64 + '\'' +
                ", item=" + item +
                '}';
    }

    public void registerUpdate(CustomItemIngredient customItemIngredient) {
        ingredients.add(customItemIngredient);
    }
}
