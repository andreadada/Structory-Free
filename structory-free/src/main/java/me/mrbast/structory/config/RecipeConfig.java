package me.mrbast.structory.config;

import me.mrbast.dadaconfig.logic.Config;
import me.mrbast.structory.Structory;
import me.mrbast.structory.config.parser.RecipeConfigParser;
import me.mrbast.structory.manager.RecipeManager;
import me.mrbast.structory.crafting.recipe.StructureRecipe;
//import me.mrbast.charms.util.Debugger;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class RecipeConfig extends Config {


    private static final RecipeConfig instance;
    static {
        try {
            instance = new RecipeConfig();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public static RecipeConfig getInstance() {return instance;}
    private static final Logger LOGGER = Structory.getPlugin(Structory.class).getLogger();





    public RecipeConfig() throws IOException, InvalidConfigurationException {
        super();
        initDirectory("recipes", true, config->{

            config.getNodes().forEach(keySection-> {
                Optional<RecipeConfigParser.RecipeParserConfig> sampleRecipe = keySection.read(RecipeConfigParser.RecipeParserConfig.class);
                sampleRecipe.ifPresent(recipe -> {
                    RecipeManager.getInstance().registerRecipe(recipe.getRecipe());

                    recipe.getGroups().forEach(group-> {
                        RecipeManager.getInstance().registerRecipeGroup(group);
                        RecipeManager.getInstance().setRecipeGroup(recipe.getRecipe(), group);
                    });
                });
            });
        });
    }

    @Override
    public void load() {

        initDirectory("recipes", true, config->{

            config.getNodes().forEach(keySection-> {
                Optional<RecipeConfigParser.RecipeParserConfig> sampleRecipe = keySection.read(RecipeConfigParser.RecipeParserConfig.class);
                sampleRecipe.ifPresent(recipe -> {
                    RecipeManager.getInstance().registerRecipe(recipe.getRecipe());

                    recipe.getGroups().forEach(group-> {
                        RecipeManager.getInstance().registerRecipeGroup(group);
                        RecipeManager.getInstance().setRecipeGroup(recipe.getRecipe(), group);
                    });
                });
            });
        });


    }
}
