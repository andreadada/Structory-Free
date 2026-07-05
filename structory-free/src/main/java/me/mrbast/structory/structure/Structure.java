package me.mrbast.structory.structure;


import me.mrbast.structory.interaction.DefaultInteraction;
import me.mrbast.structory.interaction.Interaction;
import me.mrbast.structory.manager.StructureInstanceManager;
import me.mrbast.structory.structure.layout.StructureLayout;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;


import java.util.Collection;
import java.util.UUID;

/***
 * STATIC / TEMPLATE Class, There is JUST one for every structure, works as a template.
 */
public class Structure {


    /*
    public interface StructureSupplier{ StructureLayout _new(Location location) throws CloneNotSupportedException; }
    private StructureSupplier supplier;

     */

    private StructureLayout layout;

    private Interaction interaction;

    private final StructureData data;

    public Structure(StructureData data) {
        this.data = data;
        this.interaction = new DefaultInteraction();
    }

    /*
    public Structure(StructureData structureData, StructureSupplier supplier) {
        this.supplier = supplier;
        this.data = structureData;

    }

    public StructureSupplier getSupplier() {
        return supplier;
    }

     */
    public StructureData getData() {return data;}

    public Collection<StructureInstance> getInstances() {
        return StructureInstanceManager.getInstance().getFiltered(struct -> struct.getData().getStructure().equals(this));
    }

    /***
     * Used to create easily a new structure instance.
     * @return new instance of current structure template
     */
    public StructureInstance createNewStructureInstance(){

        return new StructureInstance(this, UUID.randomUUID());
    }

    public StructureLayout getLayout() {
        return layout;
    }

    public void setLayout(StructureLayout layout) {
        this.layout = layout;
    }

    /*public void setSupplier(StructureSupplier supplier) {
        this.supplier = supplier;
    }



     */


    @Override
    public String toString() {
        return "Structure{" +
                "data=" + data +
                ", layout=" + layout +
                '}';
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }
}
