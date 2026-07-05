package me.mrbast.structory.config.parser.particle;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.particle.BoxSizedParticle;
import me.mrbast.structory.util.MathUtil;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Optional;

public class BoxSizedParticleConfigParser implements ConfigurationParser<BoxSizedParticle> {

    @Override
    public Optional<BoxSizedParticle> read(ConfigSection section, String s, Parameters parameters) {


        BoxSizedParticle particle;

        Optional<String> optCenterOffset;
        Optional<String> optStartPoint;
        Optional<String> optEndPoint;
        Optional<Integer> optAmount;
        Optional<Particle> optParticle;
        Optional<Integer> optCount;
        Optional<Double> optSpeed;

        try{
            optCenterOffset = section.read(String.class, "center-offset");
            optStartPoint = section.read(String.class, "start-point-offset");
            optEndPoint = section.read(String.class, "end-point-offset");
            optAmount = section.read(Integer.class, "amount");
            optParticle = section.read(Particle.class, "particle");
            optCount = section.read(Integer.class, "count");
            optSpeed = section.read(Double.class, "speed");
        }catch (Exception e){
            return Optional.empty();
        }

        if(!optCenterOffset.isPresent() || !optStartPoint.isPresent() || !optEndPoint.isPresent() || !optAmount.isPresent() || !optParticle.isPresent() || !optCount.isPresent())  return Optional.empty();

        Optional<Vector> centerOffset = MathUtil.parseVector(optCenterOffset.get());
        Optional<Vector> startOffset = MathUtil.parseVector(optStartPoint.get());
        Optional<Vector> endOffset = MathUtil.parseVector(optEndPoint.get());

        if(!centerOffset.isPresent() || !startOffset.isPresent() || !endOffset.isPresent()) return Optional.empty();
        particle = new BoxSizedParticle(centerOffset.get(), startOffset.get(), endOffset.get(), optAmount.get(), optParticle.get(), optCount.get(), optSpeed.orElse(0.0));


        return Optional.of(particle);

    }

    @Override
    public void write(ConfigSection configuration, String path, BoxSizedParticle object) {

    }
}
