//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.mrbast.structory.oldblockdata.newer;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlockDataRemoveEvent extends CustomBlockDataEvent {
    public CustomBlockDataRemoveEvent(@NotNull Plugin plugin, @NotNull Block block, @Nullable Event bukkitEvent) {
        super(plugin, block, bukkitEvent);
    }
}
