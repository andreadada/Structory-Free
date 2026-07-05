package me.mrbast.structory.config.parser.particle;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.particle.BoxSizedParticle;
import me.mrbast.structory.particle.FlameParticle;
import me.mrbast.structory.util.MathUtil;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Optional;

public class FlameParticleConfigParser implements ConfigurationParser<FlameParticle> {

    @Override
    public Optional<FlameParticle> read(ConfigSection section, String path, Parameters parameters) {

        FlameParticle particle;

        Optional<Vector>  centerOffset;
        Optional<Integer> optAmount;
        Optional<Particle> optParticle;
        Optional<Integer> optCount;
        Optional<Double> optSpeed;

        centerOffset = section.readString("center-offset").map(MathUtil::getVector);
        optAmount = section.read(Integer.class, "amount");
        optParticle = section.read(Particle.class, "particle");
        optCount = section.read(Integer.class, "count");
        optSpeed = section.readDouble( "speed");


        if(!optParticle.isPresent()) return Optional.empty();

        Number speed = 0d;
        if(optSpeed.isPresent()) speed = optSpeed.get();

        particle = new FlameParticle(centerOffset.orElseGet(Vector::new), optAmount.orElse(1), optParticle.get(), optCount.orElse(1), speed.doubleValue());

        return Optional.of(particle);
    }

    @Override
    public void write(ConfigSection configuration, String path, FlameParticle object) {

    }
}
