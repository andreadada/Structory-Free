package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.structure.StructureInstance;
import org.bukkit.entity.Player;

public class RecipeContext {

    private Player player;
    private StructureInstance instance;
    private Crafting crafting;


    public RecipeContext(Player player, Crafting crafting, StructureInstance instance) {
        this.player = player;
        this.instance = instance;
        this.crafting = crafting;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public StructureInstance getInstance() {
        return instance;
    }

    public void setInstance(StructureInstance instance) {
        this.instance = instance;
    }

    public Crafting getCrafting() {
        return crafting;
    }

    public void setCrafting(Crafting crafting) {
        this.crafting = crafting;
    }
}
