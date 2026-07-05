package me.mrbast.structory.crafting;

import me.mrbast.structory.crafting.decoration.CraftingDecoration;
import me.mrbast.structory.crafting.decoration.EmptyCraftingDecoration;
import me.mrbast.structory.crafting.layout.RecipeSlotLayout;
import me.mrbast.structory.crafting.recipe.craftable.DiscoveredRecipe;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CraftingSettings{

        private RecipeSlotLayout recipeSlotLayout;
        private List<DiscoveredRecipe> discoveredRecipes;
        private CraftingDecoration insert = new EmptyCraftingDecoration();
        private CraftingDecoration place= new EmptyCraftingDecoration();
        private CraftingDecoration take= new EmptyCraftingDecoration();
        private CraftingDecoration consume= new EmptyCraftingDecoration();
        private CraftingDecoration craft= new EmptyCraftingDecoration();


        public CraftingSettings(){
            this.discoveredRecipes = new ArrayList<>();
        }

        public void craft(Location location){
            if(craft != null) craft.play(location);
        }
        public void consume(Location location){
            if(consume != null) consume.play(location);
        }
        public void place(Location location){
            if(place != null) place.play(location);
        }
        public void insert(Location location){
            if(insert != null) insert.play(location);
        }
        public void take(Location location){
            if(take != null) take.play(location);
        }

        public void setConsume(CraftingDecoration consume) {
            this.consume = consume;
        }

        public void setCraft(CraftingDecoration craft) {
            this.craft = craft;
        }

        public void setInsert(CraftingDecoration insert) {
            this.insert = insert;
        }

        public void setPlace(CraftingDecoration place) {
            this.place = place;
        }

        public void setTake(CraftingDecoration take) {
            this.take = take;
        }

        public RecipeSlotLayout getRecipeSlotLayout() {
            return recipeSlotLayout;
        }

        public void setRecipeSlotLayout(RecipeSlotLayout recipeSlotLayout) {
            this.recipeSlotLayout = recipeSlotLayout;
        }

    public List<DiscoveredRecipe> getDiscoveredRecipes() {
        return discoveredRecipes;
    }

    public void setDiscoveredRecipes(List<DiscoveredRecipe> discoveredRecipes) {
        this.discoveredRecipes = discoveredRecipes;
    }
}