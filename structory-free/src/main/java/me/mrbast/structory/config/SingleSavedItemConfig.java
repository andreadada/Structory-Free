package me.mrbast.structory.config;

import me.mrbast.dadaconfig.logic.Config;
import me.mrbast.structory.Structory;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SingleSavedItemConfig extends Config {

    public static final Logger LOGGER = Structory.getPlugin(Structory.class).getLogger();

    private SavedItemProvider savedItemProvider;
    private File file;

    public SingleSavedItemConfig(SavedItemProvider savedItemProvider) {
        this.savedItemProvider = savedItemProvider;
        this.file = FileUtil.prepare("customitem/"+savedItemProvider.getKey().getKey()+".yml", true);
    }


    @Override
    public void load() {




    }

    @Override
    public void save() {
        if(!file.exists())  return;
        write(SavedItemProvider.class, "", savedItemProvider);
        try {
            super.save(file);
        } catch (IOException e) {
            LOGGER.warning("Couldn't save structure instance '"+savedItemProvider.getKey().getKey()+"' config");
        }
    }

    public void delete() {
        if(!file.exists()) return;
        file.delete();
    }
}
