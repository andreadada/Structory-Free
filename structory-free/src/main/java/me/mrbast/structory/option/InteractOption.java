package me.mrbast.structory.option;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.enums.Key;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.structure.Structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InteractOption implements Option{



    public static interface InteractionMap{

        void interact(InteractionType type);

    }
    public static enum InteractionType{

        CLICK,
        SHIFT_CLICK;

    }


    public static interface Interaction{

        void interact();

    }




    public static class DefaultInteractionMap implements InteractionMap{

        private Map<InteractionType, Interaction> interactions;

        public DefaultInteractionMap(){
            interactions = new HashMap<>();
            interactions.put(InteractionType.CLICK, new DefaultInteraction());
        }

        @Override
        public void interact(InteractionType type) {

        }
    }

    public static class DefaultInteraction  implements Interaction{


        private Set<Object> registered;

        @Override
        public void interact() {

        }
    }


    @Override
    public void read(Structure structure, ConfigSection section) {


        section.getSection("interact").ifPresent(interactSection -> {

        });



    }

    @Override
    public void write(ConfigSection configSection) {

    }

    @Override
    public Key getKey() {
        return StructureSpacedKey.OPTION_INTERACT;
    }

    @Override
    public void init(Structure structure) {

    }

    @Override
    public void init() {

    }
}
