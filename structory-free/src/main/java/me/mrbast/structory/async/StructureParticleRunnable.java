package me.mrbast.structory.async;

import me.mrbast.structory.particle.AltarParticle;
import me.mrbast.structory.structure.Structure;

import java.util.*;

public class StructureParticleRunnable implements Runnable {

    private final Map<Structure, AltarParticle> observers = Collections.synchronizedMap(new HashMap<>());


    protected StructureParticleRunnable(){

    }

    public void addObserver(final Structure structure, final AltarParticle particle) {
        this.observers.put(structure, particle);
    }

    public void removeObserver(Structure observer) {
        observers.remove(observer);
    }



    @Override
    public void run() {

        observers.entrySet().parallelStream().forEach(entry->{

            Structure structure = entry.getKey();
            AltarParticle particle = entry.getValue();

            structure.getInstances().forEach(instance ->{
              particle.show(instance.getData().getCenter());
            });

        });
    }
}