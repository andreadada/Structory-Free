package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.crafting.layout.RecipeSlotLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeSlotLayoutConfigParser implements ConfigurationParser<RecipeSlotLayout> {
    @Override
    public Optional<RecipeSlotLayout> read(ConfigSection section, String path, Parameters parameters) {

        RecipeSlotLayout layout = new RecipeSlotLayout();

        List<String> offsets = section.getStringList("offsets");
        if(offsets.isEmpty()) return Optional.empty();


        layout.setVectors(offsets.stream().map(RecipeSlotLayout::convert).collect(Collectors.toCollection(ArrayList::new)));

        return Optional.of(layout);

    }

    @Override
    public void write(ConfigSection configuration, String path, RecipeSlotLayout object) {

    }
}
