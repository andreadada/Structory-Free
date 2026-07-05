package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.crafting.recipe.ingredient.ItemIngredient;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;

import java.util.*;
import java.util.stream.Stream;

public class Acceptor {

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<ItemIngredient> itemIngredients = new ArrayList<>();

    public void addAll(List<Ingredient> acceptors) {
        acceptors.stream().filter(x->x instanceof ItemIngredient).forEach(x->itemIngredients.add((ItemIngredient)x));
        acceptors.stream().filter(x->!(x instanceof ItemIngredient)).forEach(ingredients::add);
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<ItemIngredient> getItemIngredients() {
        return itemIngredients;
    }

    public void add(Ingredient ingredient) {
        if(ingredient instanceof ItemIngredient) {

            itemIngredients.add((ItemIngredient)ingredient);
            return;
        }
        ingredients.add(ingredient);
    }

    @Override
    public String toString() {
        return "Acceptor{" +
                "ingredients=" + ingredients +
                ", itemIngredients=" + itemIngredients +
                '}';
    }

    public boolean hasDeterministic() {
        return itemIngredients.stream().anyMatch(ItemIngredient::hasDeterministic);
    }

    public Set<DeterministicItem> getDeterministicItems() {
        Map<DeterministicItem, Integer> amount = new HashMap<>();
        itemIngredients.stream().filter(ItemIngredient::hasDeterministic).map(ItemIngredient::getDeterministicItem).forEach(item->{
            amount.put(item, amount.getOrDefault(item, 0) + 1);
        });
        Set<DeterministicItem> deterministicItems = new HashSet<>();
        amount.forEach((k,v)->{
            deterministicItems.add(new DeterministicItem(k.getMaterial(), k.getTitle(), k.getModelData(), v));
        });
        return deterministicItems;
    }
}