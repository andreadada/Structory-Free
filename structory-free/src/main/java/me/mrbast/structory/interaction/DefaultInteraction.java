package me.mrbast.structory.interaction;

import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.option.InteractableOption;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultInteraction implements Interaction{

    private final Map<StructureSpacedKey, InteractListener> listeners;
    private InteractionHandle shiftRightClick;
    private InteractionHandle rightClick;


    public DefaultInteraction() {
        this.listeners = new HashMap<>();
        this.shiftRightClick = new DefaultInteractionHandle();
        this.rightClick = new DefaultInteractionHandle();
    }

    @Override
    public void handleInteract(StructureInstance structure, PlayerInteractEvent event) {


        Player player = event.getPlayer();

        InteractionHandle handle = rightClick;

        if(player.isSneaking()){
            handle = shiftRightClick;
        }

        handle.handle(structure, event);

    }

    @Override
    public void subscribeListener(InteractableOption option) {

        listeners.put(option.getKey(), option.getInteractListener());

    }

    @Override
    public Collection<InteractListener> getRegisteredListeners() {
        return listeners.values();
    }
}
