package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Structory;
import me.mrbast.structory.saveditem.SavedItemProvider;
import org.bukkit.NamespacedKey;

import java.util.Optional;

public class SavedItemProviderConfigParser implements ConfigurationParser<SavedItemProvider> {




    @Override
    public Optional<SavedItemProvider> read(ConfigSection configuration, String path, Parameters parameters) {

        Optional<NamespacedKey> val = configuration.readString("key").map(x -> new NamespacedKey(Structory.getPlugin(Structory.class), x));
        Optional<String> base64_ = configuration.readString("base64");

        return val.map(x->{
            try{
                Optional<SavedItemProvider> value = base64_.map(base64 -> new SavedItemProvider(x, base64));
                return value.orElse(null);
            }catch (Exception e){
                Structory.getPlugin(Structory.class).getLogger().severe("Error while loading '" + val.get() + "' saved item file, it can be an error due to backporting the file from newer version");
                return null;
            }


        });

    }

    @Override
    public void write(ConfigSection configuration, String path, SavedItemProvider object) {
        configuration.set("key", object.getKey().getKey());
        configuration.set("base64", object.getBase64());
    }
}
