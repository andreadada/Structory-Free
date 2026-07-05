package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Sound;

import java.util.Optional;

public class SoundConfigParser implements ConfigurationParser<Sound> {



    @Override
    public Optional<Sound> read(ConfigSection section, String path, Parameters parameters) {
        Sound sound = new Sound();

        section.readString("type").ifPresent(type->{
            org.bukkit.Sound typeSound;
            try{
                typeSound = org.bukkit.Sound.valueOf(type);
            }catch (IllegalArgumentException e){
                return;
            }

            sound.setSound(typeSound);
        });

        section.readFloat("volume").ifPresent(sound::setVolume);
        section.readFloat("pitch").ifPresent(sound::setPitch);

        return Optional.of(sound);
    }

    @Override
    public void write(ConfigSection configuration, String path, Sound object) {

    }
}
