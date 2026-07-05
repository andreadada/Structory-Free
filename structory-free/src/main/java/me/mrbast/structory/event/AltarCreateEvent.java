package me.mrbast.structory.event;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.event.player.PlayerInteractEvent;

public class AltarCreateEvent extends LoadStructureInstance{

    private PlayerInteractEvent interactEvent;

    public AltarCreateEvent(PlayerInteractEvent event, StructureInstance instance) {
        super(instance);
        this.interactEvent = event;
    }

    public PlayerInteractEvent getInteractEvent() {
        return interactEvent;
    }

    public void setInteractEvent(PlayerInteractEvent interactEvent) {
        this.interactEvent = interactEvent;
    }
}
