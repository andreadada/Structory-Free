package me.mrbast.structory.async;

import me.mrbast.structory.Structory;
import me.mrbast.structory.particle.AltarParticle;
import me.mrbast.structory.structure.Structure;
import org.bukkit.scheduler.BukkitTask;

public class StructureParticleScheduler {



    private static final StructureParticleScheduler instance = new StructureParticleScheduler();
    public static StructureParticleScheduler getInstance(){
        return instance;
    }

    private final StructureParticleRunnable runnable = new StructureParticleRunnable();
    private BukkitTask task;

    private StructureParticleScheduler(){



    }


    public void add(Structure structure, AltarParticle altarParticle) {
        runnable.addObserver(structure, altarParticle);
    }


    public synchronized void start(){
        if (task != null && !task.isCancelled()) return;
        Structory plugin = Structory.getPlugin(Structory.class);
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, 20L, 20L);
    }

    public synchronized void stop() {
        if (task == null) return;
        task.cancel();
        task = null;
    }


    public StructureParticleRunnable getRunnable() {
        return runnable;
    }

}
