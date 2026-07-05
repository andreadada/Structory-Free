package me.mrbast.structory.config;

import me.mrbast.dadaconfig.DadaConfig;
import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.Sound;
import me.mrbast.structory.Structory;
import me.mrbast.structory.config.parser.*;
import me.mrbast.structory.config.parser.particle.BoxSizedParticleConfigParser;
import me.mrbast.structory.config.parser.particle.FlameParticleConfigParser;
import me.mrbast.structory.crafting.decoration.CraftingDecoration;
import me.mrbast.structory.itembuilder.ItemBuilder;
import me.mrbast.structory.option.OptionValue;
import me.mrbast.structory.particle.BoxSizedParticle;
import me.mrbast.structory.particle.FlameParticle;
import me.mrbast.structory.particle.Particle;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;
import me.mrbast.structory.crafting.recipe.result.Result;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.structure.builder.StructureBuilder;
import me.mrbast.structory.crafting.layout.RecipeSlotLayout;
import me.mrbast.structory.structure.layout.StructureLayout;
import me.mrbast.structory.structure.layout.level.StructureLevel;
import me.mrbast.structory.structure.layout.level.StructureLevels;
import me.mrbast.structory.structure.orientation.LevelOrientation;

public class ConfigInit {


    public static void init(){


        DadaConfig.prepare(Structory.getPlugin(Structory.class));
        ConfigSection.register(Ingredient.class, new IngredientConfigParser());
        ConfigSection.register(ItemBuilder.class, new ItemBuilderConfigParser());
        ConfigSection.register(RecipeConfigParser.RecipeParserConfig.class, new RecipeConfigParser());
        ConfigSection.register(Result.class, new ResultConfigParser());
        ConfigSection.register(Structure.class, new StructureConfigParser());
        ConfigSection.register(StructureInstance.class, new StructureInstanceConfigParser());
        ConfigSection.register(StructureLayout.class, new StructureLayoutConfigParser());
        ConfigSection.register(RecipeSlotLayout.class, new RecipeSlotLayoutConfigParser());
        ConfigSection.register(StructureBuilder.class, new StructureBuilderConfigParser());
        ConfigSection.register(StructureLevels.class, new StructureLevelsConfigParser());
        ConfigSection.register(StructureLevel.class, new StructureLevelConfigParser());
        ConfigSection.register(OptionValue.class, new OptionValueConfigParser());
        ConfigSection.register(BoxSizedParticle.class, new BoxSizedParticleConfigParser());
        ConfigSection.register(FlameParticle.class, new FlameParticleConfigParser());
        ConfigSection.register(Sound.class, new SoundConfigParser());
        ConfigSection.register(Particle.class, new ParticleConfigParser());
        ConfigSection.register(CraftingDecoration.class, new CraftingDecorationConfigParser());
        ConfigSection.register(LevelOrientation.class, new LevelOrientationConfigParser());
        ConfigSection.register(SavedItemProvider.class, new SavedItemProviderConfigParser());

    }
}
