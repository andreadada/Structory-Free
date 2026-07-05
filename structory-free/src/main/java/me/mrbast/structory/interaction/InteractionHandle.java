package me.mrbast.structory.interaction;

import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.event.player.PlayerInteractEvent;

public interface InteractionHandle {


    void handle(StructureInstance structure, PlayerInteractEvent event);
}
