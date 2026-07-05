package me.mrbast.structory.option;



import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.manager.OptionManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * Used as container in MainConfig
 */
public class OptionValue {


    private final Set<Option> options = new HashSet<>();


    public OptionValue(Set<Option> options){
        if(options == null) return;
        this.options.addAll(options);
    }





    public Set<Option> getOptions() {
        return options;
    }
}
