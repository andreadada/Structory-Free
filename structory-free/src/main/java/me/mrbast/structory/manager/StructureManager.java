package me.mrbast.structory.manager;

import me.mrbast.structory.structure.Structure;
import org.bukkit.NamespacedKey;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StructureManager {


    private static final StructureManager instance = new StructureManager();
    public static StructureManager getInstance() { return instance; }

    private StructureManager() { }

    private final Map<NamespacedKey, Structure> structures = new HashMap<>();
    private final Map<String, NamespacedKey> structuresName = new HashMap<>();


    public void registerStructure(Structure structure) {
        if(structures.containsKey(structure.getData().getKey())) return;
        this.structures.put(structure.getData().getKey(), structure);
        this.structuresName.put(structure.getData().getKey().getKey(), structure.getData().getKey());


    }

    public void clear(){
        structures.clear();
        structuresName.clear();
    }

    public NamespacedKey getStructureUUIDByName(String name){
        return structuresName.get(name);
    }
    public Structure getStructureByUUID(NamespacedKey uuid){
        return structures.get(uuid);
    }
    public Structure getStructureByName(String name){
        return structures.get(structuresName.get(name));
    }

    public Optional<Structure> getStructureFiltered(Predicate<Structure> condition){
        return structures.values().stream().filter(condition).findAny();
    }

    public Collection<Structure> getStructuresFiltered(Predicate<Structure> condition){
        return structures.values().stream().filter(condition).collect(Collectors.toCollection(HashSet::new));
    }

}
