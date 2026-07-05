package me.mrbast.structory.option;

import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.interaction.InteractListener;

public interface InteractableOption {


    Option getOption();
    StructureSpacedKey getKey();
    InteractListener getInteractListener();
}
