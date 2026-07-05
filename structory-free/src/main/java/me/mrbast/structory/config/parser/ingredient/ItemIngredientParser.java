package me.mrbast.structory.config.parser.ingredient;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Structory;
import me.mrbast.structory.config.parser.IngredientConfigParser;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;
import me.mrbast.structory.crafting.recipe.ingredient.ItemIngredient;
import me.mrbast.structory.util.PersistentDataUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;



public class ItemIngredientParser extends IngredientConfigParser.IngredientParser {


    public static void generic(ItemIngredient itemIngredient, ConfigSection section, Parameters parameters){

        section.readInt("model").ifPresent(itemIngredient::model);

        section.getSection("has").ifPresent(hasSection->{

            hasSection.getNodes().forEach(keySection->{

                keySection.readString("type").ifPresent(type->{

                    PersistentDataType<Object, Object> dataType = PersistentDataUtil.getInstance().getDataType(type);
                    Optional<Object> value = keySection.read("value");

                    if(value.isPresent()){
                        itemIngredient.withData(new NamespacedKey(Structory.getPlugin(Structory.class), keySection.getName()), dataType, (x)-> x.equals(value.get()));
                    }else{
                        itemIngredient.hasData(new NamespacedKey(Structory.getPlugin(Structory.class), keySection.getName()), dataType);
                    }




                });


            });

        });

        section.readInt("amount").ifPresent(amount->parameters.add("amount", (Integer) amount));
        section.readString("displayName").ifPresent(itemIngredient::titleMatches);
        section.getSection("enchantments").ifPresent(enchantments->{

            enchantments.getKeys(false).forEach(enchantment -> {
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantment.toLowerCase()));
                if (enchant == null) return;

                @Nullable Object val = enchantments.get(enchantment);
                if(val instanceof String && val.equals("*")) itemIngredient.hasEnchant(enchant);
                else if (val instanceof Number) itemIngredient.hasEnchant(enchant, ((Number) val).intValue()) ;


            });
        });

    }

    @Override
    public Optional<Ingredient> parse(ConfigSection section, Parameters parameters) {
        ItemIngredient itemIngredient = new ItemIngredient();

        section.read(Material.class, "material").ifPresent(itemIngredient::type);

        generic(itemIngredient, section, parameters);


        return Optional.of(itemIngredient);
    }
}
