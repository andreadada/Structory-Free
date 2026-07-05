package me.mrbast.structory.structure;

import me.mrbast.structory.Structory;
import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;

import java.util.UUID;

/***
 * There is one structure instance for each ritual altar in the minecraft world
 */
public class StructureInstance {

    private static final Structory PLUGIN = Structory.getPlugin(Structory.class);

    private StructureInstanceData data = new StructureInstanceData();

    //private final Crafting crafting = new Crafting();

    public StructureInstance(Structure structure, UUID uuid) {
        this.data.setUUID(uuid);
        this.data.setStructure(structure);
    }


    public StructureInstanceData getData() {
        return data != null ? data : StructureInstanceData.empty();
    }

    public void setData(StructureInstanceData data) {
        this.data = data;
    }


    /* public Crafting getCrafting() {
        return crafting;
    }

    */


    /***
     * Prepare new state for saving the instance to manager
     * @param center center of the layout
     * @param orientation
     * @return the new Instance
     */
    public StructureInstance prepare(Location center, LevelOrientation orientation){



        //ADD ORIENTATION BEHAVIOR
        this.data.setCenter(center.clone().add(data.getStructure().getLayout().getCenterOffset()));
        this.data.setOrientation(orientation);

        return this;

    }

    public void init(){
        /*
        this.getCrafting().getRecipeSlots().putAll(new DefaultRecipeSlotGenerator().generate(this));

        getCrafting().getRecipeSlots().forEach((key, recipeslot)->{

            Block block = recipeslot.getSlotLocation().getBlock();
            CustomBlockData blockData = new CustomBlockData(block, PLUGIN);
            blockData.set(AltarSpacedKey.RECIPE_SLOT.getNamespacedKey(), PersistentDataType.STRING, this.data.getUUID().toString());
            blockData.set(AltarSpacedKey.RECIPE_SLOT_ID.getNamespacedKey(), PersistentDataType.INTEGER, key);

        });
         */

        data.getStructure().getLayout().enableBlockProtection(data.getOrientation(), this);
        data.getStructure().getLayout().build(data.getOrientation(), this.getData().getCenter());

    }

}
