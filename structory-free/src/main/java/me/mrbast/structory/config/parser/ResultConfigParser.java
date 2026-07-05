package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.dadaconfig.logic.Wrapper;
import me.mrbast.structory.itembuilder.ItemBuilder;
import me.mrbast.structory.crafting.recipe.result.CustomItemResult;
import me.mrbast.structory.crafting.recipe.result.ItemResult;
import me.mrbast.structory.crafting.recipe.result.Result;
import me.mrbast.structory.registry.TypeRegistry;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.Set;


public class ResultConfigParser implements ConfigurationParser<Result> {

    public abstract static class ResultParser{
        public abstract Optional<Result> parse(ConfigSection section);
    }

    private static final TypeRegistry<ResultParser> TYPES = new TypeRegistry<>();

    public static void register(String name, ResultParser parser){
        TYPES.register(name, parser);
    }

    public static Set<String> registeredTypes() {
        return TYPES.names();
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

        ResultParser parser = TYPES.find(type_.get()).orElse(null);
        if(parser == null) return Optional.empty();

        return parser.parse(section);
    }

    @Override
    public void write(ConfigSection configuration, String path, Result object) {

    }
}
