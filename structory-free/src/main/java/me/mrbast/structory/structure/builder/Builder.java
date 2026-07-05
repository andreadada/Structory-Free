package me.mrbast.structory.structure.builder;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Builder implements Cloneable {

    public static Optional<BuilderParser> parse(String type) {


        BuilderParser parser = builders.getOrDefault(type, null);
        return Optional.ofNullable(parser);

    }

    private static final Map<String, BuilderParser> builders = new HashMap<>();
    public static void register(String name, BuilderParser parser) {
        builders.put(name, parser);
    }

    public abstract void build(LevelOrientation orientation, Location location);

    public interface BuilderParser{

        Optional<Builder> parse(ConfigSection section, String path);
    }


    public static void init(){
        builders.put("place", PlaceBuilder.PARSER());
        builders.put("destroy", DestroyBuilder.PARSER());
    }


    @Override
    protected Builder clone() throws CloneNotSupportedException {
        return (Builder) super.clone();
    }
}
