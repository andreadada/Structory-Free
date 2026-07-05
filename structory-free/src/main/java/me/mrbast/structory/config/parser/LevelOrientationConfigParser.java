package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.structure.orientation.LevelOrientation;

import java.util.Optional;

public class LevelOrientationConfigParser implements ConfigurationParser<LevelOrientation> {



    @Override
    public Optional<LevelOrientation> read(ConfigSection configuration, String path, Parameters parameters) {
        String val = configuration.getString(path == null ?  "" : path);
        if(val == null) return Optional.empty();

        LevelOrientation orientation;
        try{
            orientation = LevelOrientation.valueOf(val);
        }catch (IllegalArgumentException e){
            return Optional.empty();
        }

        return Optional.of(orientation);
    }

    @Override
    public void write(ConfigSection configuration, String path, LevelOrientation object) {

        configuration.set(path == null ? "" : path, object.name());
    }
}
