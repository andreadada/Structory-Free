package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Sound;
import me.mrbast.structory.crafting.decoration.CraftingDecoration;
import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.particle.Particle;

import java.util.Optional;

public class CraftingDecorationConfigParser implements ConfigurationParser<CraftingDecoration> {




    @Override
    public Optional<CraftingDecoration> read(ConfigSection section, String path, Parameters parameters) {

        CraftingDecoration craftingDecoration = new CraftingDecoration();

        section.getSection("sound").flatMap(sound -> sound.read(Sound.class)).ifPresent(craftingDecoration::setSound);
        section.getSection("particle").flatMap(particle -> particle.read(Particle.class)).ifPresent(craftingDecoration::setParticle);

        return Optional.of(craftingDecoration);

    }

    @Override
    public void write(ConfigSection configuration, String path, CraftingDecoration object) {

    }
}
