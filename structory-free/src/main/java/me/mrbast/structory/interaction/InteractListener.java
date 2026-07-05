package me.mrbast.structory.interaction;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public interface InteractListener {

    void onInteract(StructureInstance instance, PlayerInteractEvent event);
}
