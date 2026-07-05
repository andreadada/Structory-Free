package me.mrbast.structory.particle;

import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class FlameParticle extends AltarParticle{

    private final Vector centerOffset;
    private final int amount;
    private final Particle particle;
    private final int count;
    private final double speed;

    public FlameParticle(Vector centerOffset, int amount, Particle particle, int count, double speed) {
        this.centerOffset = centerOffset;
        this.amount = amount;
        this.particle = particle;
        this.count = count;
        this.speed = speed;
    }


    @Override
    public void show(Location location) {

        Location loc = location.clone().add(centerOffset);


        SchedulerUtil.async(()->{
            for(int i = 0; i < amount; i++){
                Objects.requireNonNull(loc.getWorld())
                        .spawnParticle(particle,
                                loc,
                                count,
                                ThreadLocalRandom.current().nextDouble(0.15),
                                ThreadLocalRandom.current().nextDouble(0.02),
                                ThreadLocalRandom.current().nextDouble(0.15),
                                speed
                        );
            }
            SchedulerUtil.sleep(200);
        });


    }

    public static NamespacedKey key(){
        return StructureSpacedKey.PARTICLE_FLAME.getNamespacedKey();
    }
    @Override
    public NamespacedKey getKey() {
        return StructureSpacedKey.PARTICLE_FLAME.getNamespacedKey();
    }

    @Override
    public String toString() {
        return "FlameParticle{" +
                "centerOffset=" + centerOffset +
                ", amount=" + amount +
                ", particle=" + particle +
                ", count=" + count +
                ", speed=" + speed +
                '}';
    }
}
