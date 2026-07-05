package me.mrbast.structory.crafting.layout;

import me.mrbast.structory.Structory;
import me.mrbast.structory.oldblockdata.newer.CustomBlockData;
import me.mrbast.structory.crafting.recipe.RecipeSlot;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class RecipeSlotLayout {

    private static final Structory PLUGIN = Structory.getPlugin(Structory.class);

    private List<Vector> vectors = new ArrayList<>();

    public static Vector convert(String line){
        String[] pcs = line.split(" ");
        return new Vector(Double.parseDouble(pcs[0]), Double.parseDouble(pcs[1]), Double.parseDouble(pcs[2]));
    }


    public void generate(StructureInstance instance, Crafting crafting) {

        int index = 0;
        final Map<Integer, RecipeSlot> map = new HashMap<>();
        for(Vector vector : vectors){

            Orientation toAdd = instance.getData().getOrientation().from(new Orientation( vector.getX(), vector.getZ(), LevelOrientation.EAST));
            RecipeSlot recipe = new RecipeSlot(crafting, instance.getData().getCenter().clone().add(toAdd.getX().doubleValue()+0.5, vector.getY(), toAdd.getZ().doubleValue()+0.5), index);

            map.put(index, recipe);
            index++;
        }

        crafting.registerAll(map.values());

        crafting.
            forEach((recipeslot)->{
                Block block = recipeslot.getSlotLocation().getBlock();

                //USE CUSTOMBLOCKDATA
                CustomBlockData blockData = new CustomBlockData(block, PLUGIN);
                blockData.set(StructureSpacedKey.RECIPE_SLOT.getNamespacedKey(), PersistentDataType.STRING, instance.getData().getUUID().toString());
                blockData.set(StructureSpacedKey.RECIPE_SLOT_ID.getNamespacedKey(), PersistentDataType.INTEGER, recipeslot.getOrdinal());
            });

        /*
        getVectors().forEach(x->{

            int index = i.getAndIncrement();



            Orientation toAdd = instance.getData().getOrientation().from(new Orientation( x.getX(), x.getZ(), LevelOrientation.EAST));

            //Orientation blockCenterOffset = instance.getData().getOrientation().from(new Orientation( 0.5, 0.5, LevelOrientation.EAST));


            RecipeSlot recipe = new RecipeSlot(instance.getData().getCenter().clone().add(toAdd.getX().doubleValue()+0.5, x.getY(), toAdd.getZ().doubleValue()+0.5), index);
            map.put(index, recipe);

        });

         */
    }


    public void setVectors(List<Vector> vectors) {
        this.vectors = vectors;
    }

    public List<Vector> getVectors() {
        return vectors;
    }
}
