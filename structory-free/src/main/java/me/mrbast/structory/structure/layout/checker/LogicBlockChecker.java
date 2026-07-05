package me.mrbast.structory.structure.layout.checker;

import org.bukkit.Location;

public class LogicBlockChecker extends BlockChecker{

    public  enum Type{
        OR,
        AND;



    }
    private final Type type;
    private BlockChecker first;
    private BlockChecker second;

    public LogicBlockChecker(Type type,  BlockChecker first, BlockChecker second) {
        this.type = type;
        this.first = first;
        this.second = second;
    }



    @Override
    public boolean isValid(Location location) {
        return type == Type.OR ? first.isValid(location) || second.isValid(location) : first.isValid(location) && second.isValid(location);
    }

    @Override
    public String toString() {
        return "LogicBlockChecker{" +
                "type=" + type +
                ", first=" + first +
                ", second=" + second +
                '}';
    }
}
