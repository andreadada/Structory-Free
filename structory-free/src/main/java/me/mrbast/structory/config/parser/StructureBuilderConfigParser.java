package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.structure.builder.Builder;
import me.mrbast.structory.structure.builder.StructureBuilder;

import java.util.*;

public class StructureBuilderConfigParser implements ConfigurationParser<StructureBuilder> {

    @Override
    public Optional<StructureBuilder> read(ConfigSection section, String path, Parameters parameters) {
        StructureBuilder structureBuilder = new StructureBuilder();

        Set<Builder> builders = new LinkedHashSet<>();

        section.getNodes().forEach(node->{


            Optional<String> sampleType = node.readString("type");
            sampleType.ifPresent(type->{
                Optional<Builder.BuilderParser> sampleParser = Builder.parse(type.toLowerCase());
                sampleParser.ifPresent(parser->{
                    Optional<Builder> sampleBuilder = parser.parse(node, "");
                    sampleBuilder.ifPresent(builders::add);
                });

            });

        });

        structureBuilder.getBuilders().addAll(builders);


        return Optional.of(structureBuilder);
    }

    @Override
    public void write(ConfigSection configuration, String path, StructureBuilder object) {

    }
}
