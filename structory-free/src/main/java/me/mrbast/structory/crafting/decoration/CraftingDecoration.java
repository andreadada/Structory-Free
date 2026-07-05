package me.mrbast.structory.crafting.decoration;

import me.mrbast.structory.Sound;
import me.mrbast.structory.particle.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class CraftingDecoration{

        private Sound sound;
        private Particle particle;
        private Vector particleOffset = new Vector(0,0,0);

        public void play(Location location){
            if(this.sound != null) this.sound.play(location);
            Location offset = location.clone().add(particleOffset);
            if(this.particle  != null) this.particle.show(offset);
        }

        public Particle getParticle() {
            return particle;
        }

        public Sound getSound() {
            return sound;
        }

        public Vector getParticleOffset() {
            return particleOffset;
        }

        public void setParticle(Particle particle) {
            this.particle = particle;
        }

        public void setParticleOffset(Vector particleOffset) {
            this.particleOffset = particleOffset;
        }

        public void setSound(Sound sound) {
            this.sound = sound;
        }
    }