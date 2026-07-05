package me.mrbast.structory.structure.orientation;

public class Orientation {

    private final Number x;
    private final Number z;
    private final LevelOrientation orientation;

    public Orientation(Number x, Number z, LevelOrientation orientation) {
        this.x = x;
        this.z = z;
        this.orientation = orientation;
    }

    public Number getX() {
        return x;
    }

    public Number getZ() {
        return z;
    }

    public LevelOrientation getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return "Orientation{" +
                "x=" + x +
                ", z=" + z +
                ", orientation=" + orientation +
                '}';
    }
}