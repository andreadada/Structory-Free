//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.mrbast.structory.version.blockdata.newer;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CustomBlockDataEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    final Plugin plugin;
    @NotNull
    final Block block;
    @NotNull
    final CustomBlockData cbd;
    @NotNull
    final Event bukkitEvent;
    boolean isCancelled = false;

    protected CustomBlockDataEvent(@NotNull Plugin plugin, @NotNull Block block, @NotNull Event bukkitEvent) {
        this.plugin = plugin;
        this.block = block;
        this.bukkitEvent = bukkitEvent;
        //USE CUSTOMBLOCKDATA
        this.cbd = new CustomBlockData(block, plugin);
    }

    @NotNull
    public Block getBlock() {
        return this.block;
    }

    @NotNull
    public Event getBukkitEvent() {
        return this.bukkitEvent;
    }

    @NotNull
    public CustomBlockData getCustomBlockData() {
        return this.cbd;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @NotNull
    public Reason getReason() {
        if (this.bukkitEvent == null) {
            return Reason.UNKNOWN;
        } else {
            Reason[] var1 = Reason.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Reason reason = var1[var3];
                if (reason != Reason.UNKNOWN && reason.eventClasses.stream().anyMatch((clazz) -> {
                    return clazz.equals(this.bukkitEvent.getClass());
                })) {
                    return reason;
                }
            }

            return Reason.UNKNOWN;
        }
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static enum Reason {
        BLOCK_BREAK(new Class[]{BlockBreakEvent.class}),
        BLOCK_PLACE(new Class[]{BlockPlaceEvent.class, BlockMultiPlaceEvent.class}),
        EXPLOSION(new Class[]{EntityExplodeEvent.class, BlockExplodeEvent.class}),
        PISTON(new Class[]{BlockPistonExtendEvent.class, BlockPistonRetractEvent.class}),
        BURN(new Class[]{BlockBurnEvent.class}),
        ENTITY_CHANGE_BLOCK(new Class[]{EntityChangeBlockEvent.class}),
        FADE(new Class[]{BlockFadeEvent.class}),
        STRUCTURE_GROW(new Class[]{StructureGrowEvent.class}),
        FERTILIZE(new Class[]{BlockFertilizeEvent.class}),
        /** @deprecated */
        @Deprecated
        LEAVES_DECAY(new Class[]{LeavesDecayEvent.class}),
        UNKNOWN(new Class[]{(Class)null});

        @NotNull
        private final List<Class<? extends Event>> eventClasses;

        @SafeVarargs
        private Reason(Class... eventClasses) {
            this.eventClasses = Arrays.asList(eventClasses);
        }

        @NotNull
        public List<Class<? extends Event>> getApplicableEvents() {
            return this.eventClasses;
        }
    }
}
