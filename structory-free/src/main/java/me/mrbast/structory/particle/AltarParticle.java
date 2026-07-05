package me.mrbast.structory.particle;



import me.mrbast.dadaconfig.logic.ConfigSection;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

import java.util.Optional;

public abstract class AltarParticle {


    public abstract void show(Location location);
    public abstract NamespacedKey getKey();





}
