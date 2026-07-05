package me.mrbast.structory.config.parser;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.structure.layout.checker.Checker;
import me.mrbast.structory.structure.layout.level.StructureLevel;
import me.mrbast.structory.util.CheckerUtil;
import org.bukkit.Material;

import java.util.*;

public class StructureLevelConfigParser implements ConfigurationParser<StructureLevel> {
    @Override
    public Optional<StructureLevel> read(ConfigSection section, String path, Parameters parameters) {
        Optional<Integer> yLevel;
        Optional<String> type;
        try {
            yLevel = section.read(Integer.class,  "level");
            type = section.read(String.class,  "type");
        }catch (Exception e){
            return Optional.empty();
        }
        if(!yLevel.isPresent()) return Optional.empty();


        StructureLevel level = new StructureLevel(yLevel.get());

        Map<Character, Material> characterMaterialMap = new HashMap<>();
        final Set<Checker>[] mainAndSwappable = new Set[]{new HashSet<Checker>(), new HashSet<Checker>()};

        section.getSection("checkers").ifPresent(checkers->{

            checkers.getSection("types").ifPresent(types->{

                types.getKeys(false).forEach(key->{
                    types.read(Material.class, key).ifPresent(x->characterMaterialMap.put(key.charAt(0), x));
                });

            });


            if(checkers.contains("main")) mainAndSwappable[0] = CheckerUtil.parse(characterMaterialMap, checkers.getStringList("main"), false);
            if(checkers.contains("rotate")) mainAndSwappable[1] = CheckerUtil.parse(characterMaterialMap, checkers.getStringList("rotate"), true);

        });
        level = new StructureLevel(yLevel.get());
        //if(type.get().equals("ALIGNED")) level = new AlignedStructureLevel(yLevel.get());


        level.getCheckers().addAll(mainAndSwappable[0]);
        level.getCheckers().addAll(mainAndSwappable[1]);


        return Optional.of(level);
    }

    @Override
    public void write(ConfigSection configuration, String path, StructureLevel object) {

    }
}
