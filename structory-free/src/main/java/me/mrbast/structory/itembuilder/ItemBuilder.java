package me.mrbast.structory.itembuilder;

import me.mrbast.structory.Structory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {

    private Material material;
    private String displayName;
    private List<String> lore;
    private int amount = 1;
    private boolean unbreakable;
    private Integer model = null ;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Set<Consumer<ItemStack>> actions = new HashSet<>();
    private Map<String, Map<PersistentDataType<Object,Object>, Object>> tags = new HashMap<>();

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder setName(String name) {
        this.displayName = name;
        return this;
    }

    public void setTag(String key, PersistentDataType<Object,Object> dataType, Object value) {
        tags.computeIfAbsent(key, k -> new HashMap<>()).put(dataType, value);
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder addAction(Consumer<ItemStack> action) {
        this.actions.add(action);
        return this;
    }

    public ItemBuilder setModelData(int model) {
        this.model = model;
        return this;
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = this.lore != null ? this.lore : new ArrayList<>();
        lore.add(line);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {

        this.enchantments.put(enchantment, level);
        return this;

    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStack build() {


        if(material == null) return null;

        ItemStack item = new ItemStack(material);
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();
        if( meta == null) return item;

        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setUnbreakable(unbreakable);


        if(meta instanceof EnchantmentStorageMeta){
            EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
            this.enchantments.forEach((enchantment, level) -> enchantmentStorageMeta.addStoredEnchant(enchantment, level, true));

        }else this.enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));



        if(model != null) meta.setCustomModelData(model);
        tags.forEach((key, map)-> map.forEach((dataType, value)-> meta.getPersistentDataContainer().set(new NamespacedKey(Structory.getPlugin(Structory.class), key), dataType, value)));

        item.setItemMeta(meta);

        actions.forEach(c->c.accept(item));

        return item;


    }



}