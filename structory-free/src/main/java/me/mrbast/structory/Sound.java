package me.mrbast.structory;

import me.mrbast.structory.version.Version;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Vector;

public class Sound {

    private org.bukkit.Sound sound;
    private float volume;
    private float pitch;


    public void play(Player player){
        if(sound == null) return;
        Version.getInstance().playSound(player, sound, volume, pitch);
    }
    public void play(Location location){
        if(sound == null) return;
        Version.getInstance().playSound(location, sound, volume, pitch);
    }


    public void setSound(org.bukkit.Sound sound) {
        this.sound = sound;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public org.bukkit.Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
