package me.mrbast.structory.util;

import org.bukkit.util.Vector;

import java.util.Optional;

public class MathUtil {



    public static Vector getVector(String txt){


        String[] split = txt.split(" ");

        if(split.length < 3) return null;


        double x,y,z;
        try {
            x = Double.parseDouble(split[0]);
            y = Double.parseDouble(split[1]);
            z = Double.parseDouble(split[2]);
        }
        catch(Exception e){
            return null;
        }


        return new Vector(x,y,z);
    }

    public static Optional<Vector> parseVector(String txt){


        String[] split = txt.split(" ");

        if(split.length < 3) return Optional.empty();


        double x,y,z;
        try {
            x = Double.parseDouble(split[0]);
            y = Double.parseDouble(split[1]);
            z = Double.parseDouble(split[2]);
        }
        catch(Exception e){
            return Optional.empty();
        }


        return Optional.of(new Vector(x,y,z));
    }
}
