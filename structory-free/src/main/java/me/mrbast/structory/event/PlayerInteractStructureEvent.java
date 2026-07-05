package me.mrbast.structory.event;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;

public class PlayerInteractStructureEvent extends StructureEvent implements Cancellable{

    private StructureInstance instance;
    private Player player;
    private boolean cancelled;

    public PlayerInteractStructureEvent(StructureInstance instance, Player player) {
        this.instance = instance;
        this.player = player;
        this.cancelled = false;
    }

    public StructureInstance getInstance() {
        return instance;
    }

    public void setInstance(StructureInstance instance) {
        this.instance = instance;
    }


    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public Player getPlayer() {
        return player;
    }
}
