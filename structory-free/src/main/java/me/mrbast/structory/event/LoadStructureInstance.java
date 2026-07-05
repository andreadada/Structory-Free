package me.mrbast.structory.event;

import me.mrbast.structory.structure.StructureInstance;

public class LoadStructureInstance extends StructureEvent{

    private StructureInstance instance;

    public LoadStructureInstance(StructureInstance instance) {
        this.instance = instance;
    }

    public StructureInstance getInstance() {
        return instance;
    }

    public void setInstance(StructureInstance instance) {
        this.instance = instance;
    }
}
