package me.mrbast.structory.crafting.recipe;

import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.crafting.recipe.ingredient.Ingredient;
import me.mrbast.structory.crafting.recipe.ingredient.ItemIngredient;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.util.LogicUtil;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.*;

public class StructureRecipe implements Recipe{


    /**
     * Input
     */
    private Acceptor acceptor = new Acceptor();
    /**
     * Output
     */
    private Crafter crafter = new Crafter();

    private NamespacedKey key;

    private String displayName;

    private List<RecipeGroup> groups = new ArrayList<>();

    public StructureRecipe(List<Ingredient> acceptors){
        this.acceptor.addAll(acceptors);
    }

    public StructureRecipe(List<Ingredient> acceptors, Crafter crafter){
        this.acceptor.addAll(acceptors);
        this.crafter = crafter;

    }

    public StructureRecipe(){

    }



    public boolean accept(RecipeContext recipeContext){

        Crafting crafting = recipeContext.getCrafting();
        StructureInstance instance = recipeContext.getInstance();


        //RecipeContext recipeContext = new RecipeContext(player, crafting,  instance);

        crafting.setBusy(true);

        /* item fields */
        int itemAccepted = 0;
        int toFound = acceptor.getItemIngredients().stream().filter(Objects::nonNull).toList().size();
        int nonEmpty = crafting.getNonEmptyRecipeSlots();
        if(nonEmpty != toFound) return false;
        int size = crafting.size();
        Set<Integer> found = new HashSet<>();


        if(!acceptor.getItemIngredients().isEmpty()){
            for(ItemIngredient item : acceptor.getItemIngredients()){
                boolean check = true;
                boolean or = false;
                for(int i = 0; i < size; i++){
                    if(found.contains(i)) continue;
                    Optional<RecipeSlot> recipeSlot = crafting.getRecipeSlot(i);
                    if(!recipeSlot.isPresent()) continue;
                    if(!item.check(instance, recipeSlot.get())) continue;
                    or = true;
                    found.add(i);
                    break;
                }
                check = LogicUtil.and(check, or);
                if(!check) continue;
                itemAccepted++;
            }

            if(!crafting.hasFound(found)) return false;
            if(itemAccepted != acceptor.getItemIngredients().size()) return false;
        }

        /*
        Non item ingredients
         */

        int accepted = 0;
        for(Ingredient ingredient : acceptor.getIngredients()){

            boolean check = true;
            check = LogicUtil.and(check, ingredient.check(recipeContext));
            if(!check) continue;
            accepted++;
        }

        /*
        for(Map.Entry<Integer, RecipeSlot> entry : instance.getCrafting().getRecipeSlots().entrySet()){

            if(found.contains(entry.getKey())) continue;

            Optional<ItemStack> itemStack =  entry.getValue().get();
            if(itemStack.isPresent()) return false;

        }

        if(found.size() != toFound) return false;

         */



        return accepted == acceptor.getIngredients().size() ;
    }

    @Override
    public boolean isAutomaticallyDiscovered() {
        return true;
    }


    public void consume(RecipeContext recipeContext){



        acceptor.getIngredients().forEach(ingredient -> {
            SchedulerUtil.bukkitSync(()->ingredient.consume(recipeContext));
        });
        acceptor.getItemIngredients().forEach(itemIngredient -> {
            SchedulerUtil.bukkitSync(()->itemIngredient.consume(recipeContext));
            SchedulerUtil.sleep(1000);
        });


    }

    public void result(RecipeContext recipeContext){
        crafter.give(recipeContext);
    }

    @Override
    public boolean hasDisplayName() {
        return displayName != null;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Set<DeterministicItem> getDeterministicItems() {
        return acceptor.getDeterministicItems();
    }

    public NamespacedKey getKey() {
        return key;
    }

    public void setKey(NamespacedKey key) {
        this.key = key;
    }


    public Crafter getCrafter() {
        return crafter;
    }

    public Acceptor getAcceptor() {
        return acceptor;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "acceptor=" + acceptor +
                ", crafter=" + crafter +
                ", key=" + key +
                '}';
    }


    public void addRecipeGroup(RecipeGroup recipeGroup){
        this.groups.add(recipeGroup);
    }

    @Override
    public boolean hasDeterministic() {
        return acceptor.hasDeterministic();
    }


    public List<RecipeGroup> getRecipeGroups() {
        return groups;
    }



}