package me.mrbast.structory.config;

import me.mrbast.dadaconfig.logic.Config;
import me.mrbast.dadaconfig.logic.VersionableConfig;
import me.mrbast.structory.Structory;
import me.mrbast.structory.manager.SavedItemManager;
import me.mrbast.structory.manager.StructureInstanceManager;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.structure.StructureInstance;

import java.util.Optional;

public class DirectorySavedItemConfig extends VersionableConfig {

    public static final DirectorySavedItemConfig INSTANCE = new DirectorySavedItemConfig();
    public static DirectorySavedItemConfig getInstance(){return INSTANCE;}

    @Override
    public void load() {


        initDirectory("customitem", true, file->{

            try{
                Optional<SavedItemProvider> instance = file.read(SavedItemProvider.class);
                instance.ifPresent(savedItemProvider -> {
                    SavedItemManager.getInstance().register(savedItemProvider);
                });
            }catch (Exception e){
                Structory.getPlugin(Structory.class).getLogger().severe("Error while loading '" + file.getName() + "' saved item file, it can be an error due to backporting the file from newer version");
            }


        });

    }
}
