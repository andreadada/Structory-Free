package me.mrbast.structory.structure.builder;


import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import org.bukkit.Location;

public interface Offset {


    public Location getOffsetLocation(LevelOrientation orientation, Location loc);
}
