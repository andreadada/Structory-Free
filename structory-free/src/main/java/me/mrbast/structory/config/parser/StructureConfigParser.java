package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.IncConfigSection;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.Structory;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureData;
import me.mrbast.structory.structure.layout.StructureLayout;
import me.mrbast.structory.option.OptionValue;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Optional;

public class StructureConfigParser implements ConfigurationParser<Structure> {

    /***
     * Used by ConfigSection to read Structure data properly
     * @param section current section holding structure data
     * @param path should be empty or equal to section's name
     * @return Optional<Structure> holding the read Structure
     */
    public Optional<Structure> read(ConfigSection section, String path, Parameters parameters){

        String key;
        Optional<String> name;
        Optional<Material> checkBlock;
        Optional<StructureLayout> structureLayout;
        Optional<OptionValue> optionsValue;

        try{
            key = section.getName();
            name = section.read(String.class, "name");
            checkBlock = section.read(Material.class, "check-block");
            structureLayout = section.getSection("layout").flatMap(levels -> levels.read(StructureLayout.class, ""));
            optionsValue = section.getSection("options").flatMap(levels -> levels.read(OptionValue.class, ""));

        }catch (Exception e){
            return Optional.empty();
        }



        if(!name.isPresent()) return Optional.empty();


        StructureData structureData = new StructureData(new NamespacedKey(Structory.getPlugin(Structory.class), key), name.get());
        Structure structure = new Structure(structureData);

        checkBlock.ifPresent(structure.getData()::setCheckBlock);


        section.readVector("main-block").ifPresent(structureData::setMainBlockOffset);
        section.readBoolean("orientation").ifPresent(structureData::setOrientation);

        Optional<IncConfigSection> optSection = section.getSection("options");
        optSection.ifPresent(optionSection -> {

            optionsValue.ifPresent(opts-> opts.getOptions().forEach(option-> {
                Optional<IncConfigSection> sect = optionSection.getSection(option.getKey().getNamespacedKey().getKey());
                sect.ifPresent(sct-> {
                    option.read(structure, sct);
                });;
            }));


        });


        structureLayout.ifPresent(structure::setLayout);




        return Optional.of(structure);
    }

    @Override
    public void write(ConfigSection configuration, String path, Structure object) {

    }
}
