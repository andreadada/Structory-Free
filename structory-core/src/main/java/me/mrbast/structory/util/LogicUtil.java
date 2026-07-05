package me.mrbast.structory.util;

public class LogicUtil {


    public static boolean and(boolean a, boolean b){
        return a && b;
    }
    public static <T> boolean and(boolean... ands){

        for(boolean a : ands){
            if(!a) return false;
        }

        return true;
    }

    public static boolean xnor(boolean a, boolean b){
        return (a && b) || (!a && !b);
    }
}
