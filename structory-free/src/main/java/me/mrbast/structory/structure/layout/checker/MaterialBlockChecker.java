package me.mrbast.structory.structure.layout.checker;

import org.bukkit.Location;
import org.bukkit.Material;

public class MaterialBlockChecker extends BlockChecker implements Builder{


    private final Material checking;
    private final boolean inverter;

    public MaterialBlockChecker( boolean inverter, Material materialSupplier){
        this.inverter = inverter;
        this.checking = materialSupplier;
    }


    public boolean isValid(Location location) {
        return !inverter && checking.equals(location.getBlock().getType());
    }




    @Override
    public void build(Location location) {
        location.getBlock().setType(checking);
    }

    @Override
    public String toString() {
        return "MaterialBlockChecker{" +
                "checking=" + checking +
                ", inverter=" + inverter +
                '}';
    }
}
