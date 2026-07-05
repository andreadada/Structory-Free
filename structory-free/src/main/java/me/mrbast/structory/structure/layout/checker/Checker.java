package me.mrbast.structory.structure.layout.checker;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;
import org.bukkit.Location;

import java.util.Objects;

/***
 * Class that holds data and conditions
 */
public class Checker {


    protected final int xOffset;
    protected final int zOffset;
    protected final BlockChecker check;



    public Checker(int xOffset, int zOffset, BlockChecker check) {
        this.xOffset = xOffset;
        this.zOffset = zOffset;

        this.check = check;
    }

    public boolean check(Location center) {

        return check.isValid(center.clone().add(xOffset, 0, zOffset));
    }
    public boolean check(Location center, LevelOrientation levelOrientation) {

        Orientation toAdd = levelOrientation.from(new Orientation(xOffset, zOffset, LevelOrientation.EAST));

        return check.isValid(center.clone().add(toAdd.getX().doubleValue(), 0, toAdd.getZ().doubleValue()));
    }

    /*
    public void build(){
        check.prepare(location);
        check.build(location);
    }

     */

    /*
    public void ifValid(Runnable action){
        if(check(location)) action.run();
    }

     */

    /*
    public int getXOffset() {
        return xOffset;
    }

    public int getzOffset() {
        return zOffset;
    }



    public void setLocation(Location center) {
        center = center.clone().add(xOffset, 0, zOffset);
    }



    public Location getLocation() {
        return location;
    }


     */
    public BlockChecker getCheck() {
        return check;
    }




    @Override
    public String toString() {
        return "Checker{" +
                "xOffset=" + xOffset +
                ", zOffset=" + zOffset +
                ", check=" + check +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Checker checker = (Checker) object;
        return xOffset == checker.xOffset && zOffset == checker.zOffset && Objects.equals(check, checker.check);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xOffset, zOffset, check);
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getzOffset() {
        return zOffset;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
