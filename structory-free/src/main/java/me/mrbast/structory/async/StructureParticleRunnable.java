package me.mrbast.structory.async;

import me.mrbast.structory.particle.AltarParticle;
import me.mrbast.structory.structure.Structure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StructureParticleRunnable implements Runnable {

    private final Map<Structure, AltarParticle> observers = new ConcurrentHashMap<>();


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

        new ConcurrentHashMap<>(observers).forEach((structure, particle) ->
                structure.getInstances().forEach(instance ->
                        particle.show(instance.getData().getCenter())
                )
        );
    }
}
