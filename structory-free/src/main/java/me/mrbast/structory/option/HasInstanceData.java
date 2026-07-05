package me.mrbast.structory.option;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.structure.StructureInstance;

public interface HasInstanceData {


    /**
     * Method called when reading the structureinstance's data file
     *
     * This method is called before the StructureInstanceLoadEvent
     *
     * @param section
     * @param path
     * @param reference
     */
    public void load(ConfigSection section, String path, StructureInstance reference);
    public void save(ConfigSection section, String path, StructureInstance instance);

}
