package me.mrbast.structory.event;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;

public class PlayerCraftStructureEvent extends PlayerInteractStructureEvent{
    public PlayerCraftStructureEvent(StructureInstance structureInstance, Player player) {
        super(structureInstance, player);
    }
}
