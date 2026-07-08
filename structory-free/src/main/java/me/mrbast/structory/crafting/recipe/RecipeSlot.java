package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.config.MainConfig;
import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Wall;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RecipeSlot {

    /***
     * CACHING FOR PICKUP ITEM EVENT
     */
    private static final Map<Item, RecipeSlot> itemDataMap = new ConcurrentHashMap<>();
    public static Optional<RecipeSlot> getRecipeSlot(Item item){
        return Optional.ofNullable(itemDataMap.get(item));
    }


    private final Crafting crafting;

    private Location slotLocation;
    private int ordinal;

    private ItemStack originalItem;
    private Item item;

    public RecipeSlot(Crafting crafting, Location slotLocation, int number) {
        this.crafting = crafting;
        this.slotLocation = slotLocation;
        this.ordinal = number;
    }

    public StructureInstance getStructureInstance() {
        return crafting.getInstance();
    }

    public ItemData setItem(ItemStack itemStack) {

        ItemData itemData = new ItemData(this, UUID.randomUUID());
        originalItem = itemStack.clone();
        Location spawnLocation = slotLocation.clone().add(0, 1, 0);
        World world = slotLocation.getWorld();
        if( world == null) return null;
        BlockData blockData = slotLocation.getBlock().getState().getBlockData();
        if(blockData instanceof Wall || blockData instanceof Fence) spawnLocation.add(0, .5, 0);
        ItemStack dropItemstack = itemStack.clone();
        ItemMeta meta = dropItemstack.getItemMeta();
        Objects.requireNonNull(meta).getPersistentDataContainer().set(StructureSpacedKey.RECIPE_SLOT_ITEM.getNamespacedKey(), PersistentDataType.STRING, itemData.getUUID().toString());
        dropItemstack.setItemMeta(meta);

        item = world.dropItem(spawnLocation, dropItemstack);
        item.setVelocity(new Vector(0,0,0));
        item.setFallDistance(0);
        item.setGravity(false);
        itemDataMap.put(item, this);
        if(MainConfig.getInstance().disableItemPickup) item.setPickupDelay(Integer.MAX_VALUE);


        return itemData;


    }

    public void removeItem() {
        if(this.item != null) this.item.remove();

        this.originalItem = null;
        this.item = null;
    }

    public void remove(){

        /*
        Optional<Crafting> crafting =  Crafting.getCrafting(originalItem);
        crafting.ifPresent(craft->craft.remove(originalItem));
         */

        removeItem();
        clear(getEntityItem());
    }

    public void dropItem() {
        if(this.originalItem == null) return;

        ItemStack original = this.originalItem.clone();
        slotLocation.getBlock().getWorld().dropItemNaturally(slotLocation, original);
        remove();

    }

    public void clear(ItemStack itemStack){
        if(itemStack ==null) return;
        ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).getPersistentDataContainer().remove(StructureSpacedKey.RECIPE_SLOT_ITEM.getNamespacedKey());
        itemStack.setItemMeta(meta);
    }


    public Optional<ItemStack> get() {
        if(isEmpty()) return Optional.empty();
        return Optional.of(originalItem);
    }



    public boolean  isEmpty(){
        return originalItem == null || originalItem.getType() == Material.AIR;
    }
    public boolean  isPresent(){
        return originalItem != null && originalItem.getType() != Material.AIR;
    }

    public Location getSlotLocation() {
        return slotLocation;
    }

    public void setSlotLocation(Location slotLocation) {
        this.slotLocation = slotLocation;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }


    @Override
    public String toString() {
        return "RecipeSlot{" +
                "slotLocation=" + slotLocation +
                ", ordinal=" + ordinal +
                ", item=" + item +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeSlot that = (RecipeSlot) o;
        return ordinal == that.ordinal && Objects.equals(slotLocation, that.slotLocation) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotLocation, ordinal, item);
    }


    public ItemStack getEntityItem() {
        if(item == null) return null;
        return item.getItemStack();
    }


    public ItemStack getOriginal() {
        return originalItem;
    }

    public Crafting getCrafting() {
        return crafting;
    }


}
