package me.mrbast.structory.crafting.option;

import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.structory.Structory;
import me.mrbast.structory.crafting.CraftingSettings;
import me.mrbast.structory.crafting.decoration.CraftingDecoration;
import me.mrbast.structory.crafting.listener.CraftingInteractionListener;
import me.mrbast.structory.crafting.recipe.Recipe;
import me.mrbast.structory.crafting.recipe.craftable.DirectDiscoveredRecipe;
import me.mrbast.structory.crafting.recipe.craftable.DiscoveredRecipe;
import me.mrbast.structory.crafting.recipe.craftable.GroupDiscoveredRecipe;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.event.StructureEventHandler;
import me.mrbast.structory.event.Listener;
import me.mrbast.structory.event.LoadStructureInstance;
import me.mrbast.structory.interaction.InteractListener;
import me.mrbast.structory.manager.RecipeManager;
import me.mrbast.structory.option.HasInstanceData;
import me.mrbast.structory.option.InteractableOption;
import me.mrbast.structory.option.Option;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.layout.RecipeSlotLayout;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.units.qual.N;

import java.util.*;

public class CraftingOption implements Option, InteractableOption, HasInstanceData {


    private static final CraftingOption INSTANCE = new CraftingOption();

    public static CraftingOption getInstance() {
        return INSTANCE;
    }


    /**
     * Crafting real data of the structure instance
     */
    private final Map<StructureInstance, Crafting> craftingMap= new HashMap<>();
    /**
     * Structure's Crafting Settings
     */
    private final Map<Structure, CraftingSettings> craftingDataMap = new HashMap<>();

    private final Map<StructureInstance, Set<NamespacedKey>> loadeEventDiscoveredRecipe = new HashMap<>();

    private final InteractListener interactListener = ((instance, event) -> this.onInteract(event, instance));

    private CraftingOption(){

        Structory PLUGIN = Structory.getPlugin(Structory.class);
        PLUGIN.getServer().getPluginManager().registerEvents(new CraftingInteractionListener(this), PLUGIN);

    }

    public void dropAllRecipeItems() {

        craftingMap.forEach((uuid, crafting) -> {

            crafting.dropAll();

        });

    }



    @Override
    public InteractListener getInteractListener() {
        return interactListener;
    }

    public Optional<Crafting> getCrafting(StructureInstance instance) {
        return Optional.ofNullable(craftingMap.get(instance));
    }

    public void onInteract(PlayerInteractEvent event, StructureInstance instance) {
        Crafting crafting = craftingMap.get(instance);
        if(crafting == null) return;

        crafting.craftEvent(event.getPlayer());
    }


    private final Listener listener = new Listener() {


        @StructureEventHandler
        public void onLoad(LoadStructureInstance createEvent){

            StructureInstance instance = createEvent.getInstance();
            Crafting crafting = new Crafting(instance);

            CraftingSettings craftingSettings = craftingDataMap.get(instance.getData().getStructure());
            if(craftingSettings == null ) return;

            RecipeSlotLayout recipeSlotLayout = craftingSettings.getRecipeSlotLayout();
            if(recipeSlotLayout == null) return;


            recipeSlotLayout.generate(instance, crafting);
            craftingMap.put(instance, crafting);


            /*
            RECIPE FROM THE STRUCTURE
             */
            craftingSettings.getDiscoveredRecipes().forEach(discoveredRecipe ->{
                discoveredRecipe.getRecipes().forEach(crafting::discoverRecipe);
            });

            /*
            DISCOVERED RECIPE FROM THE INSTANCE
             */


            /*
            craftingSettings.getDiscoveredRecipes().forEach(discoveredRecipe -> {
                crafting.getDiscoveredRecipes().addAll(discoveredRecipe.getRecipes());
            });
            if(savedRecipes.get(instance) != null) {
                savedRecipes.get(instance).forEach(discoveredRecipe ->  {

                    crafting.getDiscoveredRecipes().addAll(discoveredRecipe.getRecipes());

                });
            }
             */


            //this.getCrafting().getRecipeSlots().putAll(new DefaultRecipeSlotGenerator().generate(this));
            /*
            getCrafting().getRecipeSlots().forEach((key, recipeslot)->{

                Block block = recipeslot.getSlotLocation().getBlock();
                CustomBlockData blockData = new CustomBlockData(block, PLUGIN);
                blockData.set(AltarSpacedKey.RECIPE_SLOT.getNamespacedKey(), PersistentDataType.STRING, this.data.getUUID().toString());
                blockData.set(AltarSpacedKey.RECIPE_SLOT_ID.getNamespacedKey(), PersistentDataType.INTEGER, key);

            });

             */

        }
    };






    @Override
    public void read(Structure structure, ConfigSection section) {

        CraftingSettings data = new CraftingSettings();

        section.getSection("insert").flatMap(insert -> insert.read(CraftingDecoration.class)).ifPresent(data::setInsert);
        section.getSection("result").flatMap(craft -> craft.read(CraftingDecoration.class)).ifPresent(data::setCraft);
        section.getSection("place").flatMap(place -> place.read(CraftingDecoration.class)).ifPresent(data::setPlace);
        section.getSection("take").flatMap(take -> take.read(CraftingDecoration.class)).ifPresent(data::setTake);
        section.getSection("consume").flatMap(consume -> consume.read(CraftingDecoration.class)).ifPresent(data::setConsume);
        section.getSection("recipe-slots").flatMap(offsets -> offsets.read(RecipeSlotLayout.class)).ifPresent(data::setRecipeSlotLayout);

        List<DiscoveredRecipe> discoveredRecipes = new ArrayList<>();
        if(section.contains("recipe-group")){
            List<String> list = section.getStringList("recipe-group");
            list.forEach(groupKey -> discoveredRecipes.add(new GroupDiscoveredRecipe(groupKey)));
        }
        if(section.contains("recipes")){
            List<String> list = section.getStringList("recipes");
            list.forEach(key -> discoveredRecipes.add(new DirectDiscoveredRecipe(key)));
        }
        if(discoveredRecipes.isEmpty()){
            discoveredRecipes.add(new GroupDiscoveredRecipe("DEFAULT"));
        }

        data.setDiscoveredRecipes(discoveredRecipes);
        structure.getInteraction().subscribeListener(this);
        craftingDataMap.put(structure, data);

    }

    @Override
    public void write(ConfigSection configSection) {

    }

    @Override
    public Option getOption() {
        return this;
    }

    @Override
    public StructureSpacedKey getKey() {
        return StructureSpacedKey.OPTION_CRAFTING;
    }

    @Override
    public void init(Structure structure) {

    }

    @Override
    public void init() {

    }


    public Optional<CraftingSettings> getCraftingData(Structure structure) {
        return Optional.ofNullable(craftingDataMap.get(structure));
    }

    public Map<Structure, CraftingSettings> getCraftingDataMap() {
        return craftingDataMap;
    }


    @Override
    public void load(ConfigSection section, String path, StructureInstance instance) {



        section.getSection("crafting").ifPresent(craftingSection -> {


            String recipes = craftingSection.getString("discoveredRecipes");
            if(recipes == null || recipes.isEmpty()) return;
            HashSet<NamespacedKey> keys = new HashSet<>();
            loadeEventDiscoveredRecipe.put(instance, keys);
            Arrays.stream(recipes.split("-")).forEach(key->{
                keys.add(NamespacedKey.fromString(key));
            });

        });


    }

    @Override
    public void save(ConfigSection section, String path, StructureInstance instance) {


        CraftingSettings settings = craftingDataMap.get(instance.getData().getStructure());


        if(!section.contains("crafting")) section.createSection("crafting");
        section.getSection("crafting").ifPresent(craftingSection -> {

            CraftingOption.getInstance().getCrafting(instance).ifPresent(crafting->{

                List<Recipe> recipes = new ArrayList<>();
                settings.getDiscoveredRecipes().forEach(discoveredRecipe -> recipes.addAll(discoveredRecipe.getRecipes()));


                StringBuilder saver = new StringBuilder();
                crafting.getDiscoveredRecipes().forEach(discoveredRecipe -> {
                    if(!recipes.contains(discoveredRecipe)) {
                        saver.append(discoveredRecipe.getKey().getKey()).append("-");
                    }

                });
                if(saver.length() == 0) return;
                saver.delete(saver.length()-1, saver.length());

                craftingSection.write(String.class, "discoveredRecipes", saver.toString());

            });

        });


    }
}
