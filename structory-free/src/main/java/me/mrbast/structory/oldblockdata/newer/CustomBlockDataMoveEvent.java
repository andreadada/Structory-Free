//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.mrbast.structory.oldblockdata.newer;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CustomBlockDataMoveEvent extends CustomBlockDataEvent {
    @NotNull
    private final Block blockTo;

    public CustomBlockDataMoveEvent(@NotNull Plugin plugin, @NotNull Block blockFrom, @NotNull Block blockTo, @NotNull Event bukkitEvent) {
        super(plugin, blockFrom, bukkitEvent);
        this.blockTo = blockTo;
    }

    @NotNull
    public Block getBlockTo() {
        return this.blockTo;
    }
}
