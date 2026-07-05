package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Structory;
import me.mrbast.structory.crafting.recipe.Recipe;
import me.mrbast.structory.crafting.recipe.StructureRecipe;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;
import me.mrbast.structory.crafting.recipe.result.Result;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeConfigParser implements ConfigurationParser<RecipeConfigParser.RecipeParserConfig> {


    public static class RecipeParserConfig {

        private Recipe recipe;
        private List<String> groups = new ArrayList<>();



        public RecipeParserConfig(StructureRecipe recipe, List<String> groups) {
            this.groups =groups;
            this.recipe = recipe;
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public List<String> getGroups() {
            return groups;
        }
    }

    @Override
    public Optional<RecipeParserConfig> read(ConfigSection section, String path, Parameters parameters) {
        Optional<String> name_ = section.readString("name");




        StructureRecipe recipe = new StructureRecipe();
        name_.ifPresent(name->{
            recipe.setKey(new NamespacedKey(Structory.getPlugin(Structory.class), name));
        });

        List<String> groups = new ArrayList<>();

        if(section.contains("group")){
            groups =  section.getStringList("group");
            if(groups.isEmpty()) groups.add(section.getString("group"));
        }else groups.add("DEFAULT");

        section.getSection("ingredients").ifPresent(ingredientSection -> {
            ingredientSection.getNodes().forEach(nodeSection->{
                Parameters params = Parameters.create();
                Optional<Ingredient> result = nodeSection.read(Ingredient.class, params);
                result.ifPresent(ingredient->{
                    for(int i = 0; i < params.get(Integer.class, "amount").orElse(1); i++) recipe.getAcceptor().add(ingredient);
                });
            });
        });


        section.getSection("result").ifPresent(ingredientSection -> {
            ingredientSection.getNodes().forEach(nodeSection->{
                Optional<Result> result_ = nodeSection.read(Result.class);
                result_.ifPresent(result->{recipe.getCrafter().add(result);});
            });
        });

        if(recipe.getKey() == null) return Optional.empty();

        RecipeParserConfig recipeParserConfig = new RecipeParserConfig(recipe, groups);
        return Optional.of(recipeParserConfig);

    }

    @Override
    public void write(ConfigSection configuration, String path, RecipeParserConfig object) {

    }
}