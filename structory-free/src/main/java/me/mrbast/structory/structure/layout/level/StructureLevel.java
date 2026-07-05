package me.mrbast.structory.structure.layout.level;


import me.mrbast.structory.structure.layout.checker.Checker;
import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;

import java.util.*;

public class StructureLevel {


    protected final int yLevel;
    protected final Set<Checker>  checkers = new HashSet<>();

    public StructureLevel(int yLevel) {
        this.yLevel = yLevel;
    }

    public boolean check(Location center){
        return checkers.stream().allMatch(checker -> checker.check(center.clone().add(0,yLevel,0), LevelOrientation.EAST));
    }
    public boolean check(Location center, LevelOrientation orientation) {

        return checkers.stream().allMatch(checker -> checker.check(center.clone().add(0,yLevel,0), orientation));
    }

    public void addChecker(Checker checker){
        this.checkers.add(checker);
    }
    public void addCheckerUnchecked(Checker checker){
        this.checkers.add(checker);
    }

    public int getYLevel() {
        return yLevel;
    }

    public Set<Checker> getCheckers() {return checkers;}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public String toString() {
        return "StructureLevel{" +
                "yLevel=" + yLevel +
                ", checkers=" + checkers +
                '}';
    }




    @Override
    public boolean equals(Object object) {

        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        StructureLevel that = (StructureLevel) object;
        return yLevel == that.yLevel && Objects.equals(checkers, that.checkers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(yLevel, checkers);
    }


}
