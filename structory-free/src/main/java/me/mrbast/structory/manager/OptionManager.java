package me.mrbast.structory.manager;

import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.option.*;
import org.bukkit.NamespacedKey;

import java.util.*;

public class OptionManager {


    public static final OptionManager instance = new OptionManager();

    public static OptionManager getInstance() {return instance;}


    private final Map<NamespacedKey, Option> options = new HashMap<>();
    private final Map<String, NamespacedKey> stringToKey = new HashMap<>();
    private final Set<HasInstanceData> hasInstanceDataSet = new HashSet<>();

    private OptionManager(){

        registerOption(StructureSpacedKey.OPTION_FIREWORK.getNamespacedKey(), new FireworksOption());
        registerOption(StructureSpacedKey.OPTION_PARTICLE.getNamespacedKey(), ParticleOption.getInstance());
        registerOption(StructureSpacedKey.OPTION_CRAFTING.getNamespacedKey(), CraftingOption.getInstance());
        registerOption(StructureSpacedKey.OPTION_NOTIFY.getNamespacedKey(), NotifyOption.getInstance());

    }






    public void registerOption(NamespacedKey key, Option option) {
        this.options.put(key, option);
        this.stringToKey.put(key.getKey(), key);
        ListenerManager.getInstance().subscribe(option);
        try{
            HasInstanceData instanceData  = (HasInstanceData) option;
            hasInstanceDataSet.add(instanceData);
        }catch (ClassCastException e){

        }
    }

    public Optional<Option> getOption(String keyStr) {
        NamespacedKey key = stringToKey.get(keyStr);
        return Optional.of(options.get(key));
    }

    public Set<HasInstanceData> getHasInstanceData() {
        return hasInstanceDataSet;
    }

    public Optional<Option> getOption(NamespacedKey key) {
        return Optional.ofNullable(options.get(key));
    }

    public void init() {

        options.values().forEach(Option::init);

    }

    public void clear() {
    }
}