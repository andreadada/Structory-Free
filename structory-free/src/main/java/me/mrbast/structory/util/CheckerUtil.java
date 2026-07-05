package me.mrbast.structory.util;

import me.mrbast.structory.Structory;
import me.mrbast.structory.structure.layout.checker.BlockChecker;
import me.mrbast.structory.structure.layout.checker.Checker;
import me.mrbast.structory.structure.layout.checker.SwappableChecker;
import org.bukkit.Material;

import java.util.*;

public class CheckerUtil {



    public static Set<Checker> parse(Map<Character, Material> characterMaterialMap, List<String> main, boolean rotate) {



        if(main.isEmpty()) return new HashSet<>();

        int colLength = main.get(0).length(); //first column length



        int rowCenter = main.size()/2;
        int colCenter = colLength/2;


        Set<Checker> checkerSet = new LinkedHashSet<>();


        for(int row = 0; row < main.size(); row++) {

            for(int column = 0; column < colLength; column++) {

                char character = main.get(row).charAt(column);
                if(character  == '*') continue;
                Material material = characterMaterialMap.get(character);

                if(material == null){
                    Structory.getPlugin(Structory.class).getLogger().info("Could not find a value for key in layout: " + character);
                    continue;
                }


                int xOffset = rowCenter-row;
                int yOffset =  column-colCenter;


                Checker toAdd = !rotate ? new Checker(xOffset, yOffset, BlockChecker.is((material))) : new SwappableChecker(xOffset, yOffset, BlockChecker.is(material));
                checkerSet.add(toAdd);

            }

        }


        return checkerSet;



    }
}
