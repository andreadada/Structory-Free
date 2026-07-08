package me.mrbast.structory.crafting.recipe.ingredient;

import me.mrbast.structory.crafting.recipe.DeterministicItem;
import me.mrbast.structory.crafting.recipe.HasRecipeSlot;
import me.mrbast.structory.crafting.recipe.RecipeContext;
import me.mrbast.structory.crafting.recipe.RecipeSlot;
import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.util.SchedulerUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Wall;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Predicate;

public class ItemIngredient extends Ingredient implements HasRecipeSlot {

    protected final Set<Predicate<ItemStack>> filters = new HashSet<>();
    private final Map<StructureInstance, RecipeSlot> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    private boolean hasDeterministic = false;
    private DeterministicItem deterministicItem;


    public void prepareDeterministic(){
        if (deterministicItem == null) deterministicItem = new DeterministicItem();
    }

    public ItemIngredient type(Material material){
        this.filters.add((item->item.getType() == material));
        setHasDeterministic(true);
        prepareDeterministic();
        deterministicItem = new DeterministicItem(material, deterministicItem.getTitle(), deterministicItem.getModelData(), deterministicItem.getAmount());
        return this;
    }


    public void setDeterministicItem(DeterministicItem deterministicItem) {
        this.deterministicItem = deterministicItem;
    }

    public ItemIngredient hasEnchant(Enchantment enchantment){
        this.filters.add(item-> {
            ItemMeta meta = item.getItemMeta();
            if(meta == null){ return false; }

            if(meta instanceof EnchantmentStorageMeta){
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                return enchantmentStorageMeta.hasStoredEnchant(enchantment);
            }
            return item.containsEnchantment(enchantment);
        });
        return this;
    }

    public ItemIngredient hasEnchant(Enchantment enchantment, int level){
        this.filters.add(item-> {

            ItemMeta meta = item.getItemMeta();
            if(meta == null){ return false; }


            if(meta instanceof EnchantmentStorageMeta){
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                return enchantmentStorageMeta.hasStoredEnchant(enchantment) && enchantmentStorageMeta.getStoredEnchantLevel(enchantment) == level;
            }

            return item.containsEnchantment(enchantment) && item.getEnchantmentLevel(enchantment) == level;
        });
        return this;
    }


    public ItemIngredient title(String title){
        this.filters.add(item-> {
            ItemMeta meta = item.getItemMeta();
            if(meta == null){ return false; }
            if(!meta.hasDisplayName()){ return false; }
            setHasDeterministic(true);
            prepareDeterministic();
            deterministicItem = new DeterministicItem(deterministicItem.getMaterial(), title, deterministicItem.getModelData(), deterministicItem.getAmount());
            return item.getItemMeta().getDisplayName().equals(title);

        });
        return this;
    }

    public ItemIngredient titleMatches(String RegEX){
        this.filters.add(item-> {
            ItemMeta meta = item.getItemMeta();
            if(meta == null){ return false; }
            if(!meta.hasDisplayName()){ return false; }
            return item.getItemMeta().getDisplayName().matches(RegEX);
        });
        return this;
    }

    public ItemIngredient hasLore(){
        this.filters.add(item-> {
            ItemMeta meta = item.getItemMeta();
            if(meta == null){ return false; }
            return meta.hasLore();
        });
        return this;
    }

    public ItemIngredient hasLore(int amount){
        this.filters.add(item-> {
            ItemMeta meta = item.getItemMeta();
            if(meta == null){ return false; }
            if(!meta.hasLore()){ return false; }
            if(meta.getLore() == null) return false;
            return meta.getLore().size() >= amount;
        });
        return this;
    }



    public <Z,T> ItemIngredient hasData(NamespacedKey key, PersistentDataType<Z,T> type){

        this.filters.add(item -> Version.getInstance().hasData(item, key, type));
        return this;
    }

    public <Z,T> ItemIngredient withData(NamespacedKey key, PersistentDataType<Z,T> type, Predicate<T> predicate){

        this.filters.add(item -> Version.getInstance().hasFilteredData(item, key, type, predicate));
        return this;
    }



    @Override
    public boolean check(StructureInstance instance, RecipeSlot recipe) {
        Optional<ItemStack> item = recipe.get();
        if(!item.isPresent()) return false;
        ItemStack itemStack = item.get();
        for(final Predicate<ItemStack> filter : filters){
            if(!filter.test(itemStack)){
                return false;
            }
        }
        CACHE.put(instance, recipe);
        return true;
    }

    @Override
    public boolean check(RecipeContext recipeContext) {
        return true;
    }

    public void model(Integer integer) {
        this.filters.add(x->{

            ItemMeta meta = x.getItemMeta();
            if( meta == null) return false;
            setHasDeterministic(true);
            prepareDeterministic();
            deterministicItem = new DeterministicItem(deterministicItem.getMaterial(), deterministicItem.getTitle(), integer, deterministicItem.getAmount());
            return meta.hasCustomModelData() && meta.getCustomModelData() == integer;

        });
    }

    @Override
    public void consume(RecipeContext recipeContext) {

        RecipeSlot slot = CACHE.get(recipeContext.getInstance());
        if(slot == null) return;

        double yOffset = 1;
        BlockData data = slot.getSlotLocation().getBlock().getState().getBlockData();
        if(data instanceof Wall || data instanceof Fence) yOffset += .5;

        double finalYOffset = yOffset;
        CraftingOption.getInstance().getCraftingData(recipeContext.getInstance().getData().getStructure()).ifPresent(craftingData->{
            craftingData.consume(slot.getSlotLocation().clone().add(0, finalYOffset, 0));
        });

        SchedulerUtil.region(slot.getSlotLocation(), slot::remove);
        CACHE.remove(recipeContext.getInstance());
    }


    @Override
    public NamespacedKey getKey() {
        return StructureSpacedKey.INGREDIENT_ITEM_ACCEPTOR.getNamespacedKey();
    }

    @Override
    public String toString() {
        return "ItemIngredient{" +
                "filters=" + filters +
                ", CACHE=" + CACHE +
                '}';
    }


    public boolean hasDeterministic() {
        return hasDeterministic;
    }

    public void setHasDeterministic(boolean hasDeterministic) {
        this.hasDeterministic = hasDeterministic;
    }

    public DeterministicItem getDeterministicItem() {
        return deterministicItem;
    }
}
