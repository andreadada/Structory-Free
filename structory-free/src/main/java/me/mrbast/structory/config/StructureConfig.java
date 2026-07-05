package me.mrbast.structory.config;

import me.mrbast.dadaconfig.logic.Config;
import me.mrbast.structory.Structory;
import me.mrbast.structory.manager.StructureManager;
import me.mrbast.structory.structure.Structure;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class StructureConfig extends Config {

    private static final StructureConfig instance;
    static {
        try {
            instance = new StructureConfig();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public static StructureConfig getInstance() {return instance;}
    private static final Logger LOGGER = Structory.getPlugin(Structory.class).getLogger();





    public StructureConfig() throws IOException, InvalidConfigurationException {
        super();
        initDirectory("structures", true, config->{

            config.getNodes().forEach(structure-> {
                Optional<Structure> struct = structure.read(Structure.class);
                struct.ifPresent(str -> {
                    StructureManager.getInstance().registerStructure(str);
                });
            });



        });
    }

    @Override
    public void load() {


        initDirectory("structures", true, config->{

            config.getNodes().forEach(structure-> {
                Optional<Structure> struct = structure.read(Structure.class);
                struct.ifPresent(str -> {
                    StructureManager.getInstance().registerStructure(str);
                });
            });



        });

    }
}
