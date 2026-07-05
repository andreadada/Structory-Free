package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.config.parser.ingredient.CustomItemIngredientParser;
import me.mrbast.structory.config.parser.ingredient.ItemIngredientParser;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;
import me.mrbast.structory.registry.TypeRegistry;

import java.util.Optional;
import java.util.Set;

public class IngredientConfigParser implements ConfigurationParser<Ingredient> {

    public abstract static class IngredientParser{
        public abstract Optional<Ingredient> parse(ConfigSection section, Parameters parameters);
    }

    private static final TypeRegistry<IngredientParser> TYPES = new TypeRegistry<>();

    public static void register(String name, IngredientParser parser){
        TYPES.register(name, parser);
    }

    public static Set<String> registeredTypes() {
        return TYPES.names();
    }



    static {

        register("saveditem", new CustomItemIngredientParser());
        register("item", new ItemIngredientParser());
    }


    @Override
    public Optional<Ingredient> read(ConfigSection section, String path, Parameters parameters) {

        Optional<String> type_ =  section.readString("type");
        if(!type_.isPresent()) return Optional.empty();


        IngredientParser parser = TYPES.find(type_.get()).orElse(null);
        if(parser == null) return Optional.empty();

        Optional<Ingredient> result = parser.parse(section, parameters);
        result.ifPresent(ingredientResult->{
            section.readBoolean("consume").ifPresent(ingredientResult::setConsume);
        });



        return result;
    }

    @Override
    public void write(ConfigSection configuration, String path, Ingredient object) {

    }
}
