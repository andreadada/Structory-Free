package me.mrbast.structory.config.parser.ingredient;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.config.parser.IngredientConfigParser;
import me.mrbast.structory.crafting.recipe.ingredient.CustomItemIngredient;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;

import java.util.Optional;

import static me.mrbast.structory.config.parser.ingredient.ItemIngredientParser.generic;


public class CustomItemIngredientParser extends IngredientConfigParser.IngredientParser {

    @Override
    public Optional<Ingredient> parse(ConfigSection section, Parameters parameters) {
        CustomItemIngredient customItemIngredient = new CustomItemIngredient();

        section.readString("key").ifPresent(customItemIngredient::key);

        generic(customItemIngredient, section, parameters);

        return Optional.of(customItemIngredient);
    }


}
