package me.mrbast.structory.oldblockdata;

import me.mrbast.structory.oldblockdata.newer.CustomBlockData;
import me.mrbast.structory.version.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PersistentDataContainerBlockData implements ICustomBlockData{

    private final CustomBlockData customBlockData;
    private final Plugin plugin;


    private static final Map<DataType, PersistentDataType<?,?>> MAP = new HashMap<>();
    static {

        MAP.put(DataType.STRING, PersistentDataType.STRING);

    }

    public PersistentDataContainerBlockData(@NotNull Block block, @NotNull Plugin plugin) {
        this.customBlockData = new CustomBlockData(block, plugin);
        this.plugin = plugin;
    }

    @Override
    public <T> void  set(String key, DataType type, T value) {
        PersistentDataType<T, T> x = (PersistentDataType<T, T>) MAP.get(type);
        customBlockData.set(new NamespacedKey(plugin, key), x, value);
    }

    @Override
    public <T> boolean has(String key, DataType type) {
        PersistentDataType<T, T> x = (PersistentDataType<T, T>) MAP.get(type);
        return customBlockData.has(new NamespacedKey(plugin, key), x);
    }

    @Override
    public <T> T get(String key, DataType type) {
        PersistentDataType<T, T> x = (PersistentDataType<T, T>) MAP.get(type);
        return customBlockData.get(new NamespacedKey(plugin, key), x);
    }
}
