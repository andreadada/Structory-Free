package me.mrbast.structory.event;

import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerBlockBreakStructureEvent extends PlayerInteractStructureEvent {

    private Block block;


    public PlayerBlockBreakStructureEvent(StructureInstance instance, Player player, Block block) {
        super(instance, player);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
