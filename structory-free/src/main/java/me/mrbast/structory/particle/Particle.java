package me.mrbast.structory.particle;

import org.bukkit.Location;

public class Particle {

    private AltarParticle altarParticle;

    public  Particle(AltarParticle altarParticle) {
        this.altarParticle = altarParticle;
    }


    public void show(Location location){
        altarParticle.show(location);
    }
}
