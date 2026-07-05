package me.mrbast.structory.structure.layout.checker;

import org.bukkit.Location;

public class SwappableChecker extends Checker{


    //TODO: FIX THIS

    public SwappableChecker(int xOffset, int zOffset, BlockChecker check) {
        super(xOffset, zOffset, check);
    }

    public boolean check(Location center) {
        return check.isValid(center.clone().add(xOffset, 0, zOffset))  || check.isValid(center.clone().subtract(zOffset, 0, xOffset));
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
