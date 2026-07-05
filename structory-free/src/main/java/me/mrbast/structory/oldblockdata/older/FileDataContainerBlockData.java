package me.mrbast.structory.oldblockdata.older;

import me.mrbast.structory.oldblockdata.ICustomBlockData;
import me.mrbast.structory.version.DataType;
import org.bukkit.block.Block;

public class FileDataContainerBlockData implements ICustomBlockData {


    private static final BlockDataContainerManager CONTAINER = new BlockDataContainerManager();
    private Block block;





    @Override
    public void set(String key, DataType type, Object value) {





    }

    @Override
    public boolean has(String key, DataType type) {
        return false;
    }

    @Override
    public <T> T get(String key, DataType type) {
        return null;
    }
}
