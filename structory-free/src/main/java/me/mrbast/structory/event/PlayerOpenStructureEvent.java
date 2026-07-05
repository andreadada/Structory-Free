package me.mrbast.structory.event;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;

public class PlayerOpenStructureEvent extends PlayerInteractStructureEvent{


    public PlayerOpenStructureEvent(StructureInstance instance, Player player) {
        super(instance, player);
    }
}
