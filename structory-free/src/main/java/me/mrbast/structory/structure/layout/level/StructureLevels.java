package me.mrbast.structory.structure.layout.level;


import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

/***
 * Class to organize more structure levels
 * levels can be STANDARD or ALIGNED with each yLevel (Warning: levels can hold different levels with same yLevel)
 * alignment is NONE by default, after check it will be NONE, X or Z.
 */
public class StructureLevels implements Cloneable{


    private final Set<StructureLevel> levels = new HashSet<>();
    public void add(StructureLevel structureLevel) {
        this.levels.add(structureLevel);
    }

    public boolean check(Location location, LevelOrientation orientation){
        return levels.stream().allMatch(level->level.check(location, orientation));
    }
    public boolean check(Location location) {
        return levels.stream().allMatch(level->level.check(location));

        /*
        alignment = alignment.swap();
        levels.forEach(x->{
            if(x  instanceof AlignedStructureLevel){
                ((AlignedStructureLevel) x).swap();
            }
        });
        return levels.stream().allMatch(structureLevel -> structureLevel.check(location));

         */

        /*
        boolean result =  levels.stream().allMatch(StructureLevel::check);
        if(!result) return false;

        //Check if every levels have same alignment.

        boolean aligned = (alignment = updateAlignment()) != AlignedStructureLevel.Alignment.ERROR;

        return aligned;



         */
    }


    /**
     * Set  alignment filed to NONE if all levels have different alignment,
     * X or Z if all levels have respectively X or Z alignment
     * @return alignment of the structure
     */
    /*
    private AlignedStructureLevel.Alignment updateAlignment(){

        AlignedStructureLevel.Alignment alignment = null;

        for(StructureLevel level : levels){

            if(!(level instanceof AlignedStructureLevel)) continue;
            if(alignment == null) {
                alignment = ((AlignedStructureLevel) level).getAlignment();
                //System.out.println("alignment set to " + alignment + " decided by " + level.getYLevel());
                continue;
            }
            if(alignment != ((AlignedStructureLevel) level).getAlignment()) {
                return AlignedStructureLevel.Alignment.ERROR;
            }
        }
        return alignment == null ? AlignedStructureLevel.Alignment.NONE : alignment;
    }


     */


    public Set<StructureLevel> getLevels() {return levels;}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public String toString() {
        return "StructureLevels{" +
                "levels=" + levels +
                '}';
    }
}
