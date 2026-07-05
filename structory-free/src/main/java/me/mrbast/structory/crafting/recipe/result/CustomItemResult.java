package me.mrbast.structory.crafting.recipe.result;

import me.mrbast.structory.crafting.recipe.RecipeContext;
import me.mrbast.structory.manager.SavedItemManager;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Wall;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import java.util.Objects;

public class CustomItemResult extends Result{

    private Vector offset;
    private String key;


    @Override
    public void craft(RecipeContext recipeContext) {
        StructureInstance instance = recipeContext.getInstance();

        SavedItemProvider savedItem = SavedItemManager.getInstance().get(key);
        if(savedItem == null) return;

        double yOffset = 1;
        Location loc = instance.getData().getCenter().clone().add(offset);
        BlockData data = loc.getBlock().getState().getBlockData();
        if(data instanceof Wall || data instanceof Fence)  yOffset = 1.5;

        loc.add(0.5, yOffset, 0.5);
        Item item = Objects.requireNonNull(loc.getWorld()).dropItem(loc, savedItem.build());
        item.setVelocity(new Vector(0,0,0));
        item.setFallDistance(0);
        item.setGravity(false);
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
