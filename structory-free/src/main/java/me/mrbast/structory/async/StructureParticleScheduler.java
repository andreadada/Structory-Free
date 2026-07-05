package me.mrbast.structory.async;

import me.mrbast.structory.Structory;
import me.mrbast.structory.particle.AltarParticle;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class StructureParticleScheduler {



    private static final StructureParticleScheduler instance = new StructureParticleScheduler();
    public static StructureParticleScheduler getInstance(){
        return instance;
    }

    private final StructureParticleRunnable runnable = new StructureParticleRunnable();

    private StructureParticleScheduler(){



    }


    public void add(Structure structure, AltarParticle altarParticle) {
        runnable.addObserver(structure, altarParticle);
    }


    public void start(){

        SchedulerUtil.scheduleRepeatingTask("particle", runnable, 1, 1,  TimeUnit.SECONDS);
        //task = Structory.getPlugin(Structory.class).getServer().getScheduler().runTaskTimerAsynchronously(Structory.getPlugin(Structory.class), runnable,  20, 20);

    }


    public StructureParticleRunnable getRunnable() {
        return runnable;
    }

}
