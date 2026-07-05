package me.mrbast.structory.particle;



import me.mrbast.structory.enums.StructureSpacedKey;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class BoxSizedParticle extends AltarParticle{

    private final Vector centerOffset;
    private final Vector startPointOffset;
    private final Vector endPointOffset;
    private final int amount;
    private final Particle particle;
    private final int count;
    private final double speed;


    public BoxSizedParticle(Vector centerOffset, Vector startPointOffset, Vector endPointOffset, int amount, Particle particle, int count, double speed) {
        this.centerOffset = centerOffset;
        this.startPointOffset = startPointOffset;
        this.endPointOffset = endPointOffset;
        this.amount = amount;
        this.particle = particle;
        this.count = count;
        this.speed = speed;
    }

    public BoxSizedParticle(Vector centerOffset, Vector startPointOffset, Vector endPointOffset, int amount, Particle particle, int count) {
        this.centerOffset = centerOffset;
        this.startPointOffset = startPointOffset;
        this.endPointOffset = endPointOffset;
        this.amount = amount;
        this.particle = particle;
        this.count = count;
        this.speed = 0;
    }

    @Override
    public void show(Location location) {

        for(int i = 0; i < amount; i++) {

            double distanceX = Math.abs(endPointOffset.getX() - startPointOffset.getX());
            double distanceY = Math.abs(endPointOffset.getY() - startPointOffset.getY());
            double distanceZ = Math.abs(endPointOffset.getZ() - startPointOffset.getZ());



            double xOffset = distanceX > 0 ?ThreadLocalRandom.current().nextDouble(distanceX) : 0;
            double yOffset = distanceY > 0 ? ThreadLocalRandom.current().nextDouble(distanceY) : 0;
            double zOffset = distanceZ > 0 ? ThreadLocalRandom.current().nextDouble(distanceZ) : 0;

            Location loc = location.clone()
                    .add(xOffset + startPointOffset.getX() ,yOffset + startPointOffset.getY(),zOffset + startPointOffset.getZ())
                    .add(centerOffset.getX() + 0.5, centerOffset.getY(), centerOffset.getZ() +0.5);
            Objects.requireNonNull(loc.getWorld()).spawnParticle(particle, loc, count);

        }


    }


    public static NamespacedKey key(){
        return StructureSpacedKey.PARTICLE_SIZEDBOX.getNamespacedKey();
    }


    @Override
    public NamespacedKey getKey() {
        return key();
    }







}
