package me.mrbast.structory.interaction;

import me.mrbast.structory.event.Listener;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;

/**
 * Check if there is only a listener to interact event, and then execute it
 * otherwise, open a menù so the player can execute what they prefer
 */
public class DefaultInteractionHandle implements InteractionHandle{


    @Override
    public void handle(StructureInstance instance, PlayerInteractEvent event) {


        Structure struct =  instance.getData().getStructure();
        Collection<InteractListener> listeners = struct.getInteraction().getRegisteredListeners();

        if(listeners.isEmpty()) return;
        if(listeners.size() == 1 ) {
            listeners.stream().findFirst().ifPresent(listener -> listener.onInteract(instance, event));
            return;
        }
        //TODO: Open menù

    }
}
