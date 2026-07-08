//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.mrbast.structory.version.blockdata.newer;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import me.mrbast.structory.util.SchedulerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomBlockData implements PersistentDataContainer {
    private static final Set<Map.Entry<UUID, BlockVector>> DIRTY_BLOCKS = ConcurrentHashMap.newKeySet();
    private static final PersistentDataType<?, ?>[] PRIMITIVE_DATA_TYPES;
    private static final NamespacedKey PERSISTENCE_KEY;
    private static final Pattern KEY_REGEX;
    private static final int CHUNK_MIN_XZ = 0;
    private static final int CHUNK_MAX_XZ = 15;
    private static final boolean HAS_MIN_HEIGHT_METHOD;
    private final PersistentDataContainer pdc;
    private final Chunk chunk;
    private final NamespacedKey key;
    private final Map.Entry<UUID, BlockVector> blockEntry;
    private final Plugin plugin;

    
    public CustomBlockData(@NotNull Block block, @NotNull Plugin plugin) {
        this.chunk = block.getChunk();
        this.key = getKey(plugin, block);
        this.pdc = this.getPersistentDataContainer();
        this.blockEntry = getBlockEntry(block);
        this.plugin = plugin;
    }

    /** @deprecated */
    @Deprecated
    public CustomBlockData(@NotNull Block block, @NotNull String namespace) {
        this.chunk = block.getChunk();
        this.key = new NamespacedKey(namespace, getKey(block));
        this.pdc = this.getPersistentDataContainer();
        this.plugin = JavaPlugin.getProvidingPlugin(CustomBlockData.class);
        this.blockEntry = getBlockEntry(block);
    }


    private static Map.Entry<UUID, BlockVector> getBlockEntry(@NotNull Block block) {
        UUID uuid = block.getWorld().getUID();
        BlockVector blockVector = new BlockVector(block.getX(), block.getY(), block.getZ());
        return new AbstractMap.SimpleEntry(uuid, blockVector);
    }

    public static boolean isDirty(Block block) {
        return DIRTY_BLOCKS.contains(getBlockEntry(block));
    }

    static void setDirty(Plugin plugin, Map.Entry<UUID, BlockVector> blockEntry) {
        DIRTY_BLOCKS.add(blockEntry);
        SchedulerUtil.global(() -> {
            DIRTY_BLOCKS.remove(blockEntry);
        });
    }

    private static NamespacedKey getKey(Plugin plugin, Block block) {
        return new NamespacedKey(plugin, getKey(block));
    }

    @NotNull
    static String getKey(@NotNull Block block) {
        int x = block.getX() & 15;
        int y = block.getY();
        int z = block.getZ() & 15;
        return "x" + x + "y" + y + "z" + z;
    }

    @Nullable
    static Block getBlockFromKey(NamespacedKey key, Chunk chunk) {
        Matcher matcher = KEY_REGEX.matcher(key.getKey());
        if (!matcher.matches()) {
            return null;
        } else {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int z = Integer.parseInt(matcher.group(3));
            return x >= 0 && x <= 15 && z >= 0 && z <= 15 && y >= getWorldMinHeight(chunk.getWorld()) && y <= chunk.getWorld().getMaxHeight() - 1 ? chunk.getBlock(x, y, z) : null;
        }
    }

    static int getWorldMinHeight(World world) {
        return HAS_MIN_HEIGHT_METHOD ? world.getMinHeight() : 0;
    }

    public static boolean hasCustomBlockData(Block block, Plugin plugin) {
        return block.getChunk().getPersistentDataContainer().has(getKey(plugin, block), PersistentDataType.TAG_CONTAINER);
    }

    public static boolean isProtected(Block block, Plugin plugin) {
        return (new CustomBlockData(block, plugin)).isProtected();
    }

    public static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new BlockDataListener(plugin), plugin);
    }

    @NotNull
    public static Set<Block> getBlocksWithCustomData(Plugin plugin, Chunk chunk) {
        NamespacedKey dummy = new NamespacedKey(plugin, "dummy");
        return getBlocksWithCustomData(chunk, dummy);
    }

    @NotNull
    private static Set<Block> getBlocksWithCustomData(@NotNull Chunk chunk, @NotNull NamespacedKey namespace) {
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        return (Set)chunkPDC.getKeys().stream().filter((key) -> {
            return key.getNamespace().equals(namespace.getNamespace());
        }).map((key) -> {
            return getBlockFromKey(key, chunk);
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @NotNull
    public static Set<Block> getBlocksWithCustomData(String namespace, Chunk chunk) {
        NamespacedKey dummy = new NamespacedKey(namespace, "dummy");
        return getBlocksWithCustomData(chunk, dummy);
    }

    public static PersistentDataType<?, ?> getDataType(PersistentDataContainer pdc, NamespacedKey key) {
        PersistentDataType[] var2 = PRIMITIVE_DATA_TYPES;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            PersistentDataType<?, ?> dataType = var2[var4];
            if (pdc.has(key, dataType)) {
                return dataType;
            }
        }

        return null;
    }

    @Nullable
    public Block getBlock() {
        World world = Bukkit.getWorld((UUID)this.blockEntry.getKey());
        if (world == null) {
            return null;
        } else {
            BlockVector vector = (BlockVector)this.blockEntry.getValue();
            return world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
        }
    }

    @NotNull
    private PersistentDataContainer getPersistentDataContainer() {
        PersistentDataContainer chunkPDC = this.chunk.getPersistentDataContainer();
        PersistentDataContainer blockPDC = (PersistentDataContainer)chunkPDC.get(this.key, PersistentDataType.TAG_CONTAINER);
        return blockPDC != null ? blockPDC : chunkPDC.getAdapterContext().newPersistentDataContainer();
    }

    public boolean isProtected() {
        return this.has(PERSISTENCE_KEY, DataType.BOOLEAN);
    }

    public void setProtected(boolean isProtected) {
        if (isProtected) {
            this.set(PERSISTENCE_KEY, DataType.BOOLEAN, true);
        } else {
            this.remove(PERSISTENCE_KEY);
        }

    }

    public void clear() {
        Set<NamespacedKey> var10000 = this.pdc.getKeys();
        PersistentDataContainer var10001 = this.pdc;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::remove);
        this.save();
    }

    private void save() {
        setDirty(this.plugin, this.blockEntry);
        if (this.pdc.isEmpty()) {
            this.chunk.getPersistentDataContainer().remove(this.key);
        } else {
            this.chunk.getPersistentDataContainer().set(this.key, PersistentDataType.TAG_CONTAINER, this.pdc);
        }

    }

    public void copyTo(Block block, Plugin plugin) {
        CustomBlockData newCbd = new CustomBlockData(block, plugin);
        this.getKeys().forEach((key) -> {
            PersistentDataType dataType = getDataType(this, key);
            if (dataType != null) {
                newCbd.set(key, dataType, this.get(key, dataType));
            }
        });
    }

    public <T, Z> void set(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
        this.pdc.set(namespacedKey, persistentDataType, z);
        this.save();
    }

    public <T, Z> boolean has(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
        return this.pdc.has(namespacedKey, persistentDataType);
    }

    public boolean has(@NotNull NamespacedKey namespacedKey) {
        PersistentDataType[] var2 = PRIMITIVE_DATA_TYPES;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            PersistentDataType<?, ?> type = var2[var4];
            if (this.pdc.has(namespacedKey, type)) {
                return true;
            }
        }

        return false;
    }

    @PaperOnly
    @Override
    public byte @NotNull [] serializeToBytes() throws IOException {
        return new byte[0];
    }
    @PaperOnly
    @Override
    public void readFromBytes(byte @NotNull [] bytes, boolean b) throws IOException {

    }
    @PaperOnly
    @Nullable
    public <T, Z> Z get(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType) {
        return this.pdc.get(namespacedKey, persistentDataType);
    }

    @NotNull
    public <T, Z> Z getOrDefault(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z z) {
        return this.pdc.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @NotNull
    public Set<NamespacedKey> getKeys() {
        return this.pdc.getKeys();
    }

    public void remove(@NotNull NamespacedKey namespacedKey) {
        this.pdc.remove(namespacedKey);
        this.save();
    }

    public boolean isEmpty() {
        return this.pdc.isEmpty();
    }

    @NotNull
    public PersistentDataAdapterContext getAdapterContext() {
        return this.pdc.getAdapterContext();
    }

    public PersistentDataType<?, ?> getDataType(NamespacedKey key) {
        return getDataType(this, key);
    }

    static {
        PRIMITIVE_DATA_TYPES = new PersistentDataType[]{PersistentDataType.BYTE, PersistentDataType.SHORT, PersistentDataType.INTEGER, PersistentDataType.LONG, PersistentDataType.FLOAT, PersistentDataType.DOUBLE, PersistentDataType.STRING, PersistentDataType.BYTE_ARRAY, PersistentDataType.INTEGER_ARRAY, PersistentDataType.LONG_ARRAY, PersistentDataType.TAG_CONTAINER_ARRAY, PersistentDataType.TAG_CONTAINER};
        PERSISTENCE_KEY = (NamespacedKey)Objects.requireNonNull(NamespacedKey.fromString("customblockdata:protected"), "Could not create persistence NamespacedKey");
        KEY_REGEX = Pattern.compile("^x(\\d+)y(-?\\d+)z(\\d+)$");
        boolean tmpHasMinHeightMethod = false;

        try {
            World.class.getMethod("getMinHeight");
            tmpHasMinHeightMethod = true;
        } catch (ReflectiveOperationException var2) {
        }

        HAS_MIN_HEIGHT_METHOD = tmpHasMinHeightMethod;
    }

    private static final class DataType {
        private static final PersistentDataType<Byte, Boolean> BOOLEAN = new PersistentDataType<Byte, Boolean>() {
            @NotNull
            public Class<Byte> getPrimitiveType() {
                return Byte.class;
            }

            @NotNull
            public Class<Boolean> getComplexType() {
                return Boolean.class;
            }

            @NotNull
            public Byte toPrimitive(@NotNull Boolean complex, @NotNull PersistentDataAdapterContext context) {
                return Byte.valueOf((byte)(complex ? 1 : 0));
            }

            @NotNull
            public Boolean fromPrimitive(@NotNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
                return primitive == 1;
            }
        };

        private DataType() {
        }
    }

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD})
    private @interface PaperOnly {
    }
}
