package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.manager.OptionManager;
import me.mrbast.structory.option.Option;
import me.mrbast.structory.option.OptionValue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OptionValueConfigParser implements ConfigurationParser<OptionValue> {
    @Override
    public Optional<OptionValue> read(ConfigSection configSection, String s, Parameters parameters) {

        Set<Option> options = configSection.getKeys(false).stream().map(key-> OptionManager.getInstance().getOption(key)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(HashSet::new));

        return Optional.of(new OptionValue(options));

    }

    @Override
    public void write(ConfigSection configuration, String path, OptionValue object) {

    }
}
