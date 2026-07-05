package me.mrbast.structory.interaction;

import me.mrbast.structory.option.InteractableOption;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;

public interface Interaction {

    void handleInteract(StructureInstance structure, PlayerInteractEvent event);


    void subscribeListener(InteractableOption option);
    Collection<InteractListener> getRegisteredListeners();

}
