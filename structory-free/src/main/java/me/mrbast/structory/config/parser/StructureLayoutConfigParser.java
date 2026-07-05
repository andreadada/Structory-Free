package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.structure.builder.StructureBuilder;
import me.mrbast.structory.crafting.layout.RecipeSlotLayout;
import me.mrbast.structory.structure.layout.StructureLayout;
import me.mrbast.structory.structure.layout.level.StructureLevels;
import me.mrbast.structory.util.MathUtil;

import java.util.Optional;

public class StructureLayoutConfigParser implements ConfigurationParser<StructureLayout> {

    @Override
    public Optional<StructureLayout> read(ConfigSection section, String path, Parameters parameters) {
        Optional<StructureLayout> layout;

        Optional<RecipeSlotLayout> recipeSlotLayout;
        Optional<StructureLevels> structureLevels;
        Optional<String> sampleCenterOffset;
        Optional<StructureBuilder> sampleStructureBuilder;

        structureLevels = section.getSection("levels").flatMap(levels -> levels.read(StructureLevels.class, ""));

        sampleCenterOffset = section.read(String.class, "center-offset");
        sampleStructureBuilder = section.getSection("build").flatMap(levels -> levels.read(StructureBuilder.class));



        StructureLayout structureLayout = new StructureLayout();
        structureLevels.ifPresent(structureLayout::setStructureLevels);
        MathUtil.parseVector(sampleCenterOffset.orElse("0 0 0")).ifPresent(structureLayout::setCenterOffset);

        //recipeSlotLayout.ifPresent(structureLayout::setRecipeSlotLayout);
        sampleStructureBuilder.ifPresent(structureLayout::setStructureBuilder);



        layout = Optional.of(structureLayout);
        return layout;

    }

    @Override
    public void write(ConfigSection configuration, String path, StructureLayout object) {

    }
}
