package me.mrbast.structory.manager;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.recipe.DeterministicItem;
import me.mrbast.structory.crafting.recipe.NamespacedKeyRecipeGroup;
import me.mrbast.structory.crafting.recipe.Recipe;
import me.mrbast.structory.crafting.recipe.RecipeGroup;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;


import java.util.*;

public class RecipeManager {


    private static final RecipeManager INSTANCE = new RecipeManager();
    public static RecipeManager getInstance() { return INSTANCE;}


    private final Map<NamespacedKey, Recipe> recipes = new HashMap<>();
    private final Map<String, NamespacedKey> recipeKeys = new HashMap<>();
    private final Map<String, RecipeGroup> recipeGroups = new HashMap<>();


    private final Set<NamespacedKey> nonDeterministicRecipes = new HashSet<>();
    /**
     * Recipes that have at least 1 deterministic filter
     * Like isTypeOf, or HasModelData, etc...
     * When you insert something that is deterministic, like insert an item it will tell you instantly what
     * recipe is that item from.
     * For example inserting: 3 wither skull
     */
    private final Map<Set<DeterministicItem>, Set<NamespacedKey>> determinisciRecipes = new HashMap<>();

    public RecipeManager() {

    }


    public void clear() {

        recipes.clear();
        recipeKeys.clear();
        recipeGroups.clear();
        nonDeterministicRecipes.clear();
        determinisciRecipes.clear();

    }

    /*
    public Collection<Recipe> getRecipeByInstanceCrafting(Player player, StructureInstance instance, Crafting crafting){
        Collection<Recipe> values = recipes.values();
        return values.stream().filter(x->x.accept(player, instance,  crafting)).collect(Collectors.toCollection(ArrayList::new));
    }

    public Collection<Recipe> getFiltered(Predicate<Recipe> condition){
        return recipes.values().stream().filter(condition).collect(Collectors.toList());
    }

    public void setRecipeGroup(Recipe recipe, String... keys){
        for(String key : keys){
            setRecipeGroup(recipe, key);
        }
    }
     */

    public Optional<Recipe> getRecipe(NamespacedKey key) {
        return Optional.ofNullable(recipes.get(key));
    }
    public Optional<Recipe> getRecipe(String key) {
        return getRecipe(recipeKeys.getOrDefault(key, null));
    }


    public Map<Set<DeterministicItem>, Set<NamespacedKey>> getDeterministicMaterialRecipes() {
        return determinisciRecipes;
    }

    public void registerRecipe(Recipe recipe) {
        this.recipes.put(recipe.getKey(), recipe);
        this.recipeKeys.put(recipe.getKey().getKey(), recipe.getKey());
        if(!recipe.hasDeterministic()) {
            nonDeterministicRecipes.add(recipe.getKey());
            return;
        }

        Set<DeterministicItem> deterministicItems = recipe.getDeterministicItems();
        Set<NamespacedKey> keys =  determinisciRecipes.computeIfAbsent(deterministicItems, k -> new HashSet<>());
        keys.add(recipe.getKey());

    }


    public void setRecipeGroup(Recipe recipe, String key){
        getRecipeGroup(key).ifPresent(recipeGroup -> {

            recipeGroup.addRecipe(recipe);
            recipe.addRecipeGroup(recipeGroup);

        });
    }

    public void registerRecipeGroup(String group) {
        recipeGroups.putIfAbsent(group, new NamespacedKeyRecipeGroup(group));
    }

    public Optional<RecipeGroup> getRecipeGroup(String key) {

        return Optional.ofNullable(recipeGroups.get(key));

    }


    public Collection<Recipe> getRecipes() {
        return recipes.values();
    }


    public Set<NamespacedKey> getDeterministicRecipeKeys(Set<DeterministicItem> deterministicItems) {


        return determinisciRecipes.get(deterministicItems);

    }

    public List<Recipe> getDeterministicRecipes(Set<DeterministicItem> deterministicItems) {


        List<Recipe> recipes = new ArrayList<>();
        Set<NamespacedKey> keys = determinisciRecipes.get(deterministicItems);


        if(keys == null) return new ArrayList<>();
        keys.forEach(key -> getRecipe(key).ifPresent(recipes::add));
        return recipes;
    }

    public Set<NamespacedKey> getNonDeterministicRecipesKey() {
        return nonDeterministicRecipes;
    }
    public Collection<Recipe> getNonDeterministicRecipes() {


        List<Recipe> recipes = new ArrayList<>();
        Set<NamespacedKey> keys = nonDeterministicRecipes;
        keys.forEach(key -> getRecipe(key).ifPresent(recipes::add));
        return recipes;

    }

    public Collection<NamespacedKey> getNonDeterministicMaterialRecipes() {
        return nonDeterministicRecipes;
    }


}
