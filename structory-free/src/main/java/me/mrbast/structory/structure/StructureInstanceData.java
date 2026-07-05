package me.mrbast.structory.structure;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

/***
 * Each single ritual altar has its own Instance Data
 * Here are variables specific to a single data.
 * See Structure to get Constant template attributes.
 */
public class StructureInstanceData {


    /***
     * Center of the level0 altar
     */
    private  UUID uuid;
    private Location center;
    private String name;
    private Structure structure;
    private LevelOrientation orientation;





    public StructureInstanceData(){

    }

    public static StructureInstanceData empty(){
        return new StructureInstanceData();
    }


    /*
    public Map<Integer, RecipeSlot> getRecipeSlots() {
        return recipeSlots;
    }

     */

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Location getMainBlockLocation() {
        return center.clone().add(structure.getData().getMainBlockOffset());
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Structure getStructure() {
        return structure;
    }

    public LevelOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(LevelOrientation orientation) {
        this.orientation = orientation;
    }
}
