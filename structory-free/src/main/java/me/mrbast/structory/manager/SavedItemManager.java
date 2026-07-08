package me.mrbast.structory.manager;

import me.mrbast.structory.Structory;
import me.mrbast.structory.config.SingleSavedItemConfig;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

public class SavedItemManager {

    private static final SavedItemManager instance = new SavedItemManager();
    public static SavedItemManager getInstance() { return instance; }
    private SavedItemManager() { }
    private final Map<NamespacedKey, SavedItemProvider> items = new ConcurrentHashMap<>();
    private final Map<String, NamespacedKey> keys = new ConcurrentHashMap<>();

    public void register(SavedItemProvider item) {

        if(items.size() >= 10) return;

        items.put(item.getKey(), item);
        keys.put(item.getKey().getKey(), item.getKey());

    }

    public void clear() {

        items.clear();
        keys.clear();

    }

    public SavedItemProvider prepare(ItemStack stack){

        return prepare(UUID.randomUUID().toString(), stack);

    }
    public SavedItemProvider prepare(String name, ItemStack stack){

        if(name == null)  name = UUID.randomUUID().toString();
        NamespacedKey namespacedKey = new NamespacedKey(Structory.getPlugin(Structory.class), name);
        return new SavedItemProvider(namespacedKey, stack);

    }

    public boolean has(String itemName) {
        return keys.containsKey(itemName);
    }

    public SavedItemProvider delete(String itemName) {
        NamespacedKey val = keys.remove(itemName);
        if(val == null) return null;
        return items.remove(val);
    }

    public SavedItemProvider get(String key) {
        NamespacedKey val = keys.get(key);
        if(val == null) return null;
        return items.get(val);
    }

    public Collection<SavedItemProvider> getAll() {
        return items.values();
    }

    public void delete(SavedItemProvider provider) {
        keys.remove(provider.getKey().getKey());
        items.remove(provider.getKey());
    }

    public void replace(String itemName, ItemStack savedItem) {

        SavedItemProvider itemProvider = get(itemName);
        if(itemProvider == null){
            itemProvider = prepare(itemName, savedItem);
            register(itemProvider);
            SavedItemProvider finalItemProvider = itemProvider;
            SchedulerUtil.async(()-> SchedulerUtil.async(()->new SingleSavedItemConfig(finalItemProvider).save()));
            return;
        }
        itemProvider.changeItem(savedItem);
    }


}
