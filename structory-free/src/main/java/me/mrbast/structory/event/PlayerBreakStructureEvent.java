package me.mrbast.structory.event;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;


public class PlayerBreakStructureEvent extends PlayerBlockBreakStructureEvent implements Cancellable {


    private boolean cancelled = false;

    public PlayerBreakStructureEvent(Player player, StructureInstance structure, Block block) {
        super(structure, player, block);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
