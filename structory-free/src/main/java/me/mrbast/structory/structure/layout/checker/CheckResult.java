package me.mrbast.structory.structure.layout.checker;

import me.mrbast.structory.structure.orientation.LevelOrientation;
import me.mrbast.structory.structure.orientation.Orientation;

public class CheckResult {


    private boolean valid;
    private LevelOrientation orientation;


    public CheckResult(boolean valid, LevelOrientation orientation) {
        this.valid = valid;
        this.orientation = orientation;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public LevelOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(LevelOrientation orientation) {
        this.orientation = orientation;
    }
}
