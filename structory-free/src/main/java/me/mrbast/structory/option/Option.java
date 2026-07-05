package me.mrbast.structory.option;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.Structory;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.enums.Key;
import me.mrbast.structory.structure.Structure;

public interface Option{


    void read(Structure structure, ConfigSection configSection);
    void write(ConfigSection configSection);



    Key getKey();

    /***
     * After reading structure's config of this specific option
     * @param structure
     */
    void init(Structure structure);


    /**
     * Called when the Option is initialized
     */
    void init();

}
