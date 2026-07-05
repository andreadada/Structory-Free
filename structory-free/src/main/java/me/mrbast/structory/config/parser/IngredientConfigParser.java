package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Structory;
import me.mrbast.structory.config.parser.ingredient.CustomItemIngredientParser;
import me.mrbast.structory.config.parser.ingredient.ItemIngredientParser;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IngredientConfigParser implements ConfigurationParser<Ingredient> {

    public abstract static class IngredientParser{
        public abstract Optional<Ingredient> parse(ConfigSection section, Parameters parameters);
    }

    private static final Map<NamespacedKey, IngredientParser> ingredients = new HashMap<>();
    private static final Map<String, NamespacedKey> keys = new HashMap<>();
    public static void register(String name, IngredientParser parser){
        NamespacedKey key = new NamespacedKey(Structory.getPlugin(Structory.class), name);
        keys.put(name, key);
        ingredients.put(key, parser);
    }



    static {

        register("saveditem", new CustomItemIngredientParser());
        register("item", new ItemIngredientParser());
    }


    @Override
    public Optional<Ingredient> read(ConfigSection section, String path, Parameters parameters) {

        Optional<String> type_ =  section.readString("type");
        if(!type_.isPresent()) return Optional.empty();


        IngredientParser parser = ingredients.get(keys.get(type_.get().toLowerCase()));
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
