package me.mrbast.structory.config;

import me.mrbast.dadaconfig.logic.Config;
import me.mrbast.structory.Structory;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SingleStructureInstanceConfig extends Config {
    public static final Logger LOGGER = Structory.getPlugin(Structory.class).getLogger();

    private final StructureInstance structureInstance;
    private File file;
    public SingleStructureInstanceConfig(StructureInstance structureInstance) {
        this.structureInstance = structureInstance;
        this.file = FileUtil.prepare("instances/"+structureInstance.getData().getUUID()+".yml", true);
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {
        if(!file.exists())  return;
        write(StructureInstance.class, "", structureInstance);
        try {
            super.save(file);
        } catch (IOException e) {
            LOGGER.warning("Couldn't save structure instance '"+structureInstance.getData().getUUID()+"' config");
        }
    }

    public void delete() {
        if(!file.exists()) return;
        file.delete();
    }
}
