package me.mrbast.structory.structure.builder;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import me.mrbast.structory.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Optional;

public class DestroyBuilder extends Builder implements Offset{


    private final Vector offset;

    public DestroyBuilder(Vector offset) {
        this.offset = offset;
    }

    @Override
    public void build(LevelOrientation orientation, Location location) {
        getOffsetLocation(orientation, location).getBlock().setType(Material.AIR);
    }

    public static BuilderParser PARSER(){
        return (section, path) -> {
            Optional<String> sampleVector = section.readString("offset");
            Optional<Vector> vector = MathUtil.parseVector(sampleVector.get());
            return vector.map(value -> new DestroyBuilder(value));
        };
    }

    @Override
    public Location getOffsetLocation(LevelOrientation orientation, Location loc) {
        Orientation toAdd = orientation.from(new Orientation(offset.getBlockX(), offset.getBlockZ(), LevelOrientation.EAST));
        return loc.clone().add(toAdd.getX().doubleValue(), 0, toAdd.getZ().doubleValue());
    }
}
