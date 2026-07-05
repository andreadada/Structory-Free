package me.mrbast.structory.crafting.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class DeterministicItem implements Cloneable {


    private final Material material;
    private final String title;
    private final long modelData;
    private final int amount;


    public DeterministicItem(ItemStack itemStack){

        this.material = itemStack.getType();
        this.amount = itemStack.getAmount();


        ItemMeta im = itemStack.getItemMeta();
        if(im == null) {
            title = null;
            modelData = 0;
            return;
        }


        if(im.hasDisplayName()) this.title = im.getDisplayName();
        else title = null;

        if(im.hasCustomModelData()) this.modelData = im.getCustomModelData();
        else modelData = 0;

    }

    public DeterministicItem(){
        material = null;
        title = null;
        modelData = 0;
        amount = 0;
    }

    public DeterministicItem(Material material, String title, long modelData, int amount){
        this.material = material;
        this.title = title;
        this.modelData = modelData;
        this.amount = amount;
    }

    public Material getMaterial() {
        return material;
    }

    public String getTitle() {
        return title;
    }


    public long getModelData() {
        return modelData;
    }


    public int getAmount() {
        return amount;
    }



    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DeterministicItem that = (DeterministicItem) object;
        return modelData == that.modelData && amount == that.amount && material.equals(that.material) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, title, modelData, amount);
    }

    @Override
    public String toString() {
        return "DeterministicItem{" +
                "material=" + material +
                ", title='" + title + '\'' +
                ", modelData=" + modelData +
                ", amount=" + amount +
                '}';
    }

}
