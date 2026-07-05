package me.mrbast.structory.structure;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.util.Vector;

import java.util.UUID;

/***
 * Holds constant value of a structure template
 */
public class StructureData {


    private NamespacedKey key;
    private String name;
    /***
     * Block type check to activate by shift + click
     */
    private Material checkBlock;
    private Vector mainBlockOffset = new Vector(0,0,0);
    private boolean orientation = true;

    public StructureData(NamespacedKey key, String name) {
        this.key = key;
        this.name = name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public void setKey(NamespacedKey key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getCheckBlock() {
        return checkBlock;
    }

    public void setCheckBlock(Material checkBlock) {
        this.checkBlock = checkBlock;
    }


    @Override
    public String toString() {
        return "StructureData{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", checkBlock=" + checkBlock +
                '}';
    }

    public Vector getMainBlockOffset() {
        return mainBlockOffset;
    }

    public void setMainBlockOffset(Vector mainBlockOffset) {
        this.mainBlockOffset = mainBlockOffset;
    }

    public boolean hasOrientation() {
        return orientation;
    }

    public void setOrientation(boolean orientation) {
        this.orientation = orientation;
    }
}
