package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.dadaconfig.logic.Wrapper;
import me.mrbast.structory.Structory;
import me.mrbast.structory.itembuilder.ItemBuilder;
import me.mrbast.structory.crafting.recipe.result.CustomItemResult;
import me.mrbast.structory.crafting.recipe.result.ItemResult;
import me.mrbast.structory.crafting.recipe.result.Result;
import org.bukkit.NamespacedKey;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ResultConfigParser implements ConfigurationParser<Result> {

    public abstract static class ResultParser{
        public abstract Optional<Result> parse(ConfigSection section);
    }

    private static final Map<NamespacedKey, ResultParser> ingredients = new HashMap<>();
    private static final Map<String, NamespacedKey> keys = new HashMap<>();
    public static void register(String name, ResultParser parser){
        NamespacedKey key = new NamespacedKey(Structory.getPlugin(Structory.class), name);
        keys.put(name, key);
        ingredients.put(key, parser);
    }
    static {

        register("saveditem", new ResultParser() {
            @Override
            public Optional<Result> parse(ConfigSection section) {
                CustomItemResult itemResult = new CustomItemResult();

                section.readString("key").ifPresent(itemResult::setKey);
                section.readVector("offset").ifPresent(itemResult::setOffset);

                return Optional.of(itemResult);

            }
        });
        register("item", new ResultParser() {
            @Override
            public Optional<Result> parse(ConfigSection section) {

                Wrapper<ItemResult> itemResultWrapper = new Wrapper<>();
                section.getSection("item").ifPresent(itemSection->{
                    ItemResult itemResult = new ItemResult();
                    Optional<ItemBuilder> itemBuilder =  itemSection.read(ItemBuilder.class);
                    itemBuilder.ifPresent(itemResult::setItemStack);
                    itemResultWrapper.set(itemResult);
                });
                Optional<Vector> offset_ = section.readVector("offset");
                if(offset_.isPresent()){
                    Vector offset = offset_.get();
                    itemResultWrapper.ifPresent(x->x.setOffset(offset));
                }

                return Optional.ofNullable(itemResultWrapper.get());

            }
        });

    }


    @Override
    public Optional<Result> read(ConfigSection section, String path, Parameters parameters) {
        Optional<String> type_ =  section.readString("type");
        if(!type_.isPresent()) return Optional.empty();

        ResultParser parser = ingredients.get(keys.get(type_.get().toLowerCase()));
        if(parser == null) return Optional.empty();

        return parser.parse(section);
    }

    @Override
    public void write(ConfigSection configuration, String path, Result object) {

    }
}
