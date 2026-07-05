package me.mrbast.structory.structure.builder;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class StructureBuilder implements Cloneable {


    private Set<Builder> builders = new HashSet<>();




    public void build(LevelOrientation orientation, Location location){
        builders.forEach(builder -> builder.build(orientation, location));
    }

    public Set<Builder> getBuilders() {
        return builders;
    }



    @Override
    public StructureBuilder clone() throws CloneNotSupportedException {
        return (StructureBuilder) super.clone();
    }

    @Override
    public String toString() {
        return "StructureBuilder{" +
                "builders=" + builders +
                '}';
    }
}
