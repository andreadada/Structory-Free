package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.structure.layout.level.StructureLevel;
import me.mrbast.structory.structure.layout.level.StructureLevels;

import java.util.Optional;

public class StructureLevelsConfigParser implements ConfigurationParser<StructureLevels> {
    @Override
    public Optional<StructureLevels> read(ConfigSection section, String path, Parameters parameters){


        StructureLevels structureLevels = new StructureLevels();
        section.getNodes().forEach(level-> {
            level.read(StructureLevel.class).ifPresent(structureLevels::add);
        });

        return Optional.of(structureLevels);
    }

    @Override
    public void write(ConfigSection configuration, String path, StructureLevels object) {

    }
}
