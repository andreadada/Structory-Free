package me.mrbast.structory.crafting;

import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.crafting.recipe.*;
import me.mrbast.structory.manager.RecipeManager;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Crafting {

    private final StructureInstance instance;
    //private List<DiscoveredRecipe> discoveredRecipes;
    private Set<NamespacedKey> discoveredRecipes;
    private final Map<Integer, RecipeSlot> recipeSlots = new HashMap<>();

    private boolean busy = false;


    public Crafting(StructureInstance instance) {
        this.instance = instance;
        discoveredRecipes = new HashSet<>();
    }


    public void craftEvent(Player player) {


        RecipeContext recipeContext = new RecipeContext(player, this, instance);


        List<ItemStack> items = new ArrayList<>();
        recipeSlots.values().forEach(recipeSlot -> recipeSlot.get().ifPresent(items::add));

        Map<DeterministicItem, Integer> map = new HashMap<>();
        items.forEach(itemStack ->  map.put(new DeterministicItem(itemStack), map.getOrDefault(new DeterministicItem(itemStack), 0) + 1));
        Set<DeterministicItem> deterministicItems = new HashSet<>();
        map.forEach((k,v)->{
            deterministicItems.add(new DeterministicItem(k.getMaterial(), k.getTitle(), k.getModelData(), v));
        });






        List<Recipe> validRecipes = RecipeManager.getInstance().getDeterministicRecipes(deterministicItems);

        if (validRecipes.isEmpty()) {


            /*
            Non ho trovato nessun recipe deterministico, allora vuol dire che può esserci qualcosa di non deterministico.
            Perchè tutto quello che ha almeno 1 item (deterministico) allora sarebbe già stato trovato.

             */

            validRecipes = RecipeManager.getInstance().getNonDeterministicRecipes().stream().filter(x->x.accept(recipeContext)).toList();


        }



        if(validRecipes.isEmpty()) {
            this.setBusy(false);
            return;
        }


        validRecipes = validRecipes.stream().filter(this::hasDiscoveredRecipe).filter(recipe -> recipe.accept(recipeContext)).toList();
        this.setBusy(false);

        validRecipes.stream().findAny().ifPresent(recipe -> {
            craft(recipeContext, recipe);
        });

         /*
         List<Recipe> validRecipes = RecipeManager.getInstance().getRecipes().stream().filter(recipe -> recipe.accept(player, instance, this)).toList();
        if (validRecipes.isEmpty()) {
            this.setBusy(false);
            return;
        }


        getDiscoveredRecipes().stream().filter(recipe -> recipe.accept(player, instance, this)).toList();
        if (validRecipes.isEmpty()) {
            this.setBusy(false);
            return;
        }
          */







    }


    public boolean hasDiscoveredRecipe(Recipe recipe) {
        return discoveredRecipes.contains(recipe.getKey());
    }

    public void craft(RecipeContext recipeContext, Recipe recipe){

        SchedulerUtil.asyncThenSync(()->{
            recipe.consume(recipeContext);
        }, ()-> {
            CraftingOption.getInstance().getCraftingData(instance.getData().getStructure()).ifPresent(craftingData->{
                craftingData.craft(instance.getData().getCenter().clone().add(0.5, 1, 0.5));
            });
            recipe.result(recipeContext);
            this.setBusy(false);
        });
    }



    public List<Recipe> getDiscoveredRecipes() {
        return discoveredRecipes.stream()
                .map(name -> RecipeManager.getInstance().getRecipe(name))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }


    public void discoverRecipe(Recipe recipe) {
        discoveredRecipes.add(recipe.getKey());
    }
    public void discoverRecipe(RecipeGroup recipe) {
        recipe.getRecipes().forEach(this::discoverRecipe);
    }


    public void forEach(Consumer<RecipeSlot> consumer) {
        recipeSlots.forEach((key,  value)-> consumer.accept(value));
    }


    public void register(RecipeSlot slot) {
        recipeSlots.put(slot.getOrdinal(), slot);
    }
    public void registerAll(Collection<RecipeSlot> values) {
        for (RecipeSlot slot : values) {
            register(slot);
        }
    }



    public boolean hasFound(Set<Integer> items){
        for(Map.Entry<Integer, RecipeSlot> entry : recipeSlots.entrySet()){
            if(items.contains(entry.getKey())) continue;

            Optional<ItemStack> itemStack =  entry.getValue().get();
            if(itemStack.isPresent()) return false;
        }
        return true;
    }

    public int size(){
        return recipeSlots.size();
    }



    public Optional<RecipeSlot> getFirstEmptyRecipeSlot() {
        return recipeSlots.values().stream().filter(RecipeSlot::isEmpty).findFirst();
    }

    public boolean add(ItemStack item){



        if(isFull()) return false;


        Optional<RecipeSlot> slotOptional = getFirstEmptyRecipeSlot();
        return slotOptional.filter(recipeSlot -> set(item, recipeSlot)).isPresent();
    }

    public ItemStack replace(int slot, ItemStack item) {


        RecipeSlot recipeSlot = recipeSlots.get(slot);
        if(recipeSlot == null) return null;
        ItemStack toReturn = null;

        if(!recipeSlot.isEmpty()){

            toReturn = recipeSlot.getOriginal();
            recipeSlot.remove();

        }

        if(item != null && item.getType() != Material.AIR) set(item, recipeSlot);

        /*
        ItemData itemData = this.craftingBySlot.get(slot);

        if(itemData != null){
            toReturn = itemData.getItem();
            crafting.remove(itemData.getItem());
            //craftingBySlot.remove(slot);
            itemDataMap.remove(itemData.getItem());

            itemData.getRecipeSlot().removeItem();
            itemData.getRecipeSlot().clear(itemData.getItem());
        }
        if(item != null && item.getType() != Material.AIR) set(item, recipeSlot);

         */
        return toReturn;



    }


    public ItemStack take(Integer slot) {
        RecipeSlot recipeSlot = recipeSlots.get(slot);
        if(recipeSlot == null) return null;
        //ItemData itemData = this.craftingBySlot.get(slot);
        ItemStack toReturn = recipeSlot.getOriginal();

        recipeSlot.remove();

        return toReturn;
    }

    /*
    private boolean set(int slot, ItemStack item){



        if(isFull()) return false;


        Optional<RecipeSlot> slotOptional = getRecipeSlot(slot);
        return slotOptional.filter(recipeSlot -> set(item, recipeSlot)).isPresent();
    }

     */

    private boolean set(ItemStack item, RecipeSlot recipeSlot) {

        if(recipeSlot == null) return false;

        ItemData itemData = recipeSlot.setItem(item);

        itemData.setCrafting(this);
        itemData.setItem(item);



        //Crafting.itemDataMap.put(recipeSlot.getEntityItem(), this);
        //this.crafting.put(item, recipeSlot.getOrdinal());
        //this.craftingBySlot.put(recipeSlot.getOrdinal(), itemData);


        return true;
    }

    /***
     * @deprecated
     * @param item
     */
    public void remove(ItemStack item){


        /*
        Integer slot = this.crafting.get(item);
        if(slot == null) return;
        ItemData itemData = craftingBySlot.get(slot);
        if(itemData == null) return;

        crafting.remove(item);
        craftingBySlot.remove(slot);
        itemDataMap.remove(itemData.getItem());


         */
        //itemData.getRecipeSlot().removeItem();
        //itemData.getRecipeSlot().clear(item);




        /*
                //Integer slot = this.crafting.get(item);
        Set<Integer> set = this.crafting.get(item);
        if(set == null) return;

        if(set.isEmpty()) return;

        Optional<Integer> first = set.stream().findFirst();

        first.ifPresent(slot->{
            ItemData itemData = craftingBySlot.get(slot);
            if(itemData == null) return;

            crafting.remove(item);
            craftingBySlot.remove(slot);
            itemDataMap.remove(itemData.getItem());

            itemData.getRecipeSlot().removeItem();
            itemData.getRecipeSlot().clear(item);
            set.remove(slot);

            if(set.isEmpty()) this.crafting.remove(item);
        });

         */

    }


    public int getNonEmptyRecipeSlots(){
        return recipeSlots.values().stream().filter(RecipeSlot::isPresent).toList().size();
    }

    public boolean isFull(){
        return recipeSlots.values().stream().allMatch(RecipeSlot::isPresent);
    }

    public Optional<RecipeSlot> getRecipeSlot(Integer slot) {

        return Optional.ofNullable(recipeSlots.get(slot));

    }


    public void dropAll() {

        /*
        recipeSlots.values().forEach(recipeSlot -> {

            ItemData data = craftingBySlot.get(recipeSlot.getOrdinal());
            if(data != null && data.getItem() != null){
                remove(data.getItem());
                Objects.requireNonNull(recipeSlot.getSlotLocation().getWorld()).dropItemNaturally(recipeSlot.getSlotLocation(),  data.getItem());
            }
        });

         */



        recipeSlots.values().forEach(RecipeSlot::dropItem);

    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }


    public StructureInstance getInstance() {
        return instance;
    }



}
