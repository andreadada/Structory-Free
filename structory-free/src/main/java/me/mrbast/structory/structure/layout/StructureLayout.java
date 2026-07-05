package me.mrbast.structory.structure.layout;

import me.mrbast.structory.structure.builder.Offset;
import me.mrbast.structory.structure.builder.StructureBuilder;
import me.mrbast.structory.structure.layout.checker.CheckResult;
import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.structure.grief.StructureProtection;
import me.mrbast.structory.structure.layout.level.StructureLevel;
import me.mrbast.structory.structure.layout.level.StructureLevels;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/***
 * Check for a layout in the world
 */
public class StructureLayout implements Cloneable{


    //protected RecipeSlotLayout recipeSlotLayout;

    protected StructureLevels structureLevels = new StructureLevels();

    protected StructureBuilder  structureBuilder = new StructureBuilder();

    protected Vector centerOffset = new Vector(0,0,0);

    public StructureLayout(){

    }


    public StructureLayout(StructureLevels clone, StructureBuilder structureBuilder) {
        this.structureLevels = clone;
        this.structureBuilder = structureBuilder;
    }

    public StructureLayout(Location location) {

    }

    public CheckResult check(Location location, boolean orientation){

        if(!orientation) return new CheckResult(structureLevels.check(location, LevelOrientation.EAST), LevelOrientation.EAST);
        for(LevelOrientation value : LevelOrientation.values()){
            if(structureLevels.check(location, value)) return new CheckResult(true, value);
        }

        return new CheckResult(false, null);
    }


    public StructureLevels getStructureLevels() {
        return structureLevels;
    }

    public StructureBuilder getStructureBuilder() {
        return structureBuilder;
    }

    public void setStructureBuilder(StructureBuilder structureBuilder) {
        this.structureBuilder = structureBuilder;
    }

    public void setStructureLevels(StructureLevels structureLevel) {
        this.structureLevels = structureLevel;
    }

    public static  <T extends StructureLevel> T prepareStructureLevel(Class<T> clazz, int level, Location  center) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<T> constructor = clazz.getConstructor(Integer.class, Location.class);
        constructor.setAccessible(true);
        return constructor.newInstance(level, center);
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "StructureLayout{" +
                ", structureLevels=" + structureLevels +
                ", structureBuilder=" + structureBuilder +
                ", centerOffset=" + centerOffset +
                '}';
    }

    public Vector getCenterOffset() {
        return centerOffset;
    }

    public void setCenterOffset(Vector centerOffset) {
        this.centerOffset = centerOffset;
    }

    public void enableBlockProtection(LevelOrientation orientation, StructureInstance instance) {


        structureLevels.getLevels().forEach(level ->{

            level.getCheckers().forEach(checker ->{
                Location loc = instance.getData().getCenter().clone();

                Orientation toAdd = orientation.from(new Orientation(checker.getXOffset(), checker.getzOffset(), LevelOrientation.EAST));

                Block block = loc.add(toAdd.getX().doubleValue(), level.getYLevel(),  toAdd.getZ().doubleValue()).getBlock();

                StructureProtection.getInstance().add(block.getLocation(), instance);;
            });
        });

        structureBuilder.getBuilders().forEach(builder -> {

            if(!(builder instanceof Offset)) return;

            Offset offset = (Offset) builder;

            StructureProtection.getInstance().add(offset.getOffsetLocation(orientation, instance.getData().getCenter()), instance);


        });

    }




    public void build(LevelOrientation orientation, Location center) {


        this.structureBuilder.build(orientation, center);
    }

    /*
    public void setRecipeSlotLayout(RecipeSlotLayout recipeSlotLayout) {
        this.recipeSlotLayout = recipeSlotLayout;
    }

    public RecipeSlotLayout getRecipeSlotLayout() {
        return recipeSlotLayout;
    }

     */
}
