package me.mrbast.structory.structure.builder;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import me.mrbast.structory.util.MathUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Optional;

public class PlaceBuilder extends Builder implements Offset{


    private final Vector vector;
    private final Material material;

    public PlaceBuilder(Vector vector, Material material) {
        this.material = material;
        this.vector = vector;
    }

    @Override
    public void build(LevelOrientation orientation, Location location) {


        Block block = getOffsetLocation(orientation, location).getBlock();
        block.setType(material);
    }

    @Override
    public Location getOffsetLocation(LevelOrientation orientation, Location loc) {
        Orientation toAdd = orientation.from(new Orientation(vector.getBlockX(), vector.getBlockZ(), LevelOrientation.EAST));
        return loc.clone().add(toAdd.getX().doubleValue(), vector.getY(), toAdd.getZ().doubleValue());
    }



    public static BuilderParser PARSER(){
        return (section, path) -> {
            Optional<Material> material = section.read(Material.class, "material");
            Optional<Vector> vector = section.readVector("offset");
            return vector.map(value -> {

                Optional<Builder> val = material.map(block -> {
                    if(block == Material.PLAYER_HEAD){
                         return new HeadBuilder(value, section.readString("texture").orElse(""));
                    }
                    return new PlaceBuilder(value, block);
                });

                return val.orElse(null);
            });
        };
    }


}

