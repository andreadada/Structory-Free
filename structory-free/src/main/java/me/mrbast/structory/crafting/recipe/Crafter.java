package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.crafting.recipe.result.Result;
import me.mrbast.structory.structure.StructureInstance;

import java.util.HashSet;
import java.util.Set;

public class Crafter {


    private Set<Result> result = new HashSet<Result>();

    public Crafter(Set<Result> result) {
        this.result.addAll(result);
    }

    public Crafter(){

    }

    public void give(RecipeContext instance) {

        result.forEach(result -> result.craft(instance));
    }

    @Override
    public String toString() {
        return "Crafter{" +
                "result=" + result +
                '}';
    }

    public void add(Result result) {
        this.result.add(result);
    }
}
