package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.option.ParticleOption;
import me.mrbast.structory.particle.AltarParticle;
import me.mrbast.structory.particle.Particle;

import java.util.Optional;

public class ParticleConfigParser implements ConfigurationParser<Particle> {



    @Override
    public Optional<Particle> read(ConfigSection section, String path, Parameters parameters) {

        Optional<String> type = section.readString("type");
        if(!type.isPresent()) return Optional.empty();

        Class<? extends AltarParticle> clazz = ParticleOption.getInstance().getParticleClass(type.get());
        Optional<? extends AltarParticle> val = section.read(clazz, "", Parameters.create());

        return val.map(Particle::new);
    }

    @Override
    public void write(ConfigSection configuration, String path, Particle object) {

    }
}
