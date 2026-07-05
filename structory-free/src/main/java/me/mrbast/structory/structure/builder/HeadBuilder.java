package me.mrbast.structory.structure.builder;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import me.mrbast.structory.util.MathUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Optional;

public class HeadBuilder extends Builder implements Offset{


    private final Vector vector;
    private final String texture;

    public HeadBuilder(Vector vector, String texture) {
        this.texture = texture;
        this.vector = vector;
    }

    @Override
    public void build(LevelOrientation orientation, Location location) {


        Block block = getOffsetLocation(orientation, location).getBlock();
        Version.getInstance().setSkullBlock(block, texture);
    }

    @Override
    public Location getOffsetLocation(LevelOrientation orientation, Location loc) {
        Orientation toAdd = orientation.from(new Orientation(vector.getBlockX(), vector.getBlockZ(), LevelOrientation.EAST));
        return loc.clone().add(toAdd.getX().doubleValue(), vector.getY(), toAdd.getZ().doubleValue());
    }

    @Override
    public String toString() {
        return "HeadBuilder{" +
                "vector=" + vector +
                ", texture='" + texture + '\'' +
                '}';
    }



    /*
    public static BuilderParser PARSER(){
        return (section, path) -> {
            Optional<String> sampleString = section.readString("texture");
            Optional<String> sampleVector = section.readString("offset");
            if(sampleString.isEmpty() || sampleVector.isEmpty()) return Optional.empty();
            Optional<Vector> vector = MathUtil.parseVector(sampleVector.get());
            String texture = sampleString.get();
            return vector.map(value -> new HeadBuilder(value, texture));
        };
    }

     */


}
