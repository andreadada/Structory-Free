package me.mrbast.structory.option;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.Structory;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.event.StructureEventHandler;
import me.mrbast.structory.event.AltarCreateEvent;
import me.mrbast.structory.event.Listener;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.util.ColorUtil;
import me.mrbast.structory.util.FireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;


import java.util.*;

public class FireworksOption implements Option{


    public interface FireworksSupplierToInstance {

        void spawn(StructureInstance instance);

    }


    public static class Fireworks{


        private FireworksSupplierToInstance fireworksSupplier;


        public Fireworks(int amount, int power, boolean flicker, Color[] mainColor, Color[] fadeColor) {

            Bukkit.getServer().getScheduler().runTaskAsynchronously(Structory.getPlugin(Structory.class), ()->{
                fireworksSupplier = instance -> {
                    for(int i = 0; i < amount; i++) {

                        FireworkUtil.prepareFirework(instance.getData().getCenter().clone().add(0.5, 1.5,0.5), 0, flicker, mainColor, fadeColor).detonate();
                    }
                };

            });

        }
    }

    private static final Map<Structure, Fireworks> structures = new HashMap<>();




    @Override
    public void read(Structure structure, ConfigSection section) {


        Optional<String> optType = Optional.empty();
        Optional<Integer> optAmount = Optional.empty();
        Optional<Integer> optPower = Optional.empty();
        Optional<String> optFade = Optional.empty();
        Optional<String> optColors = Optional.empty();
        Optional<Boolean> optFlicker = Optional.empty();

        try{
            optType = section.read(String.class, "type");
            optAmount = section.read(Integer.class, "amount");
            optPower = section.read(Integer.class, "power");
            optFade = section.read(String.class, "fade");
            optColors = section.read(String.class, "colors");
            optFlicker = section.read(Boolean.class, "flicker");
        }catch (Exception e){

        }


        final Optional<String> finalOptColors = optColors;
        final Optional<String> finalOptFade = optFade;

        int power;
        power = optPower.orElse(0);

        int amount;
        amount = optAmount.orElse(0);

        boolean flicker;
        flicker = optFlicker.orElse(false);


        optType.ifPresent(type->{

            switch (type){

                case "RANDOM":{


                    Color[] colors = new Color[0];
                    if(finalOptColors.isPresent()){
                        String[] clrs = finalOptColors.get().split(", ");
                        colors = new Color[clrs.length];
                        for(int i = 0; i < clrs.length; i++){
                            colors[i] = ColorUtil.getColor(clrs[i]);
                        }
                    }

                    Color[] fades = new Color[0];
                    if(finalOptFade.isPresent()) {
                        String[] clrs = finalOptFade.get().split(", ");
                        fades = new Color[clrs.length];
                        for (int i = 0; i < clrs.length; i++) {
                            fades[i] = ColorUtil.getColor(clrs[i]);
                        }
                    }


                    String clrs = finalOptColors.orElse("");
                    String fds = finalOptFade.orElse("");
                    structures.put(structure, new Fireworks(amount, power, flicker, colors, fades));

                }break;

            }


        });




    }

    @Override
    public void write(ConfigSection configSection) {

    }

    @Override
    public StructureSpacedKey getKey() {
        return StructureSpacedKey.OPTION_FIREWORK;
    }

    private static final Listener eventListener = new Listener() {


        @StructureEventHandler
        public void onEventCreate(AltarCreateEvent event){


            Fireworks fireworks = structures.get(event.getInstance().getData().getStructure());
            if(fireworks == null) return;
            fireworks.fireworksSupplier.spawn(event.getInstance());

        }

    };


    public void init(Structure structure) {


    }

    @Override
    public void init() {

    }
}
