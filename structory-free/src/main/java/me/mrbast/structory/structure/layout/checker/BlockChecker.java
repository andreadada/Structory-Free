package me.mrbast.structory.structure.layout.checker;

import org.bukkit.Location;
import org.bukkit.Material;

/***
 * JUST CONDITION UTIL CLASS
 */
public abstract class BlockChecker {

    public static class EverythingBlockChecker extends BlockChecker {

        EverythingBlockChecker() {
        }

        /*
        @Override
        public void build(Location location) {

        }

         */

        @Override
        public boolean isValid(Location location) {
            return true;
        }
    }

    public static BlockChecker everything() {
        return new EverythingBlockChecker();
    }

    public BlockChecker or(BlockChecker other) {
        return new LogicBlockChecker(LogicBlockChecker.Type.OR, this, other);
    }

    public BlockChecker and(BlockChecker other) {
        return new LogicBlockChecker(LogicBlockChecker.Type.AND, this, other);
    }

    public static BlockChecker isNot(Material toCheck) {
        return new MaterialBlockChecker(true, toCheck);
    }
    public static BlockChecker is(Material toCheck) {
        return new MaterialBlockChecker(false, toCheck);
    }

    //public abstract void build(Location location);
    public abstract boolean isValid(Location location);

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}