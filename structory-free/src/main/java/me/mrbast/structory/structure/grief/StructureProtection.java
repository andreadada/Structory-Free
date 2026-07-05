package me.mrbast.structory.structure.grief;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.Location;
import org.bukkit.Material;


import java.util.HashMap;
import java.util.Map;

public class StructureProtection {


    private static final StructureProtection INSTANCE = new StructureProtection();
    public static StructureProtection getInstance() {return INSTANCE;}

    private StructureProtection() {}


    private final Map<Location, StructureInstance> blocks = new HashMap<Location, StructureInstance>();

    public void add(Location location, StructureInstance structure) {
        this.blocks.put(location, structure);
    }

    public void disable(StructureInstance instance) {
        blocks.entrySet().removeIf(entry -> entry.getValue().equals(instance));
    }

    public StructureInstance get(Location location) {
        return blocks.get(location);
    }
}
