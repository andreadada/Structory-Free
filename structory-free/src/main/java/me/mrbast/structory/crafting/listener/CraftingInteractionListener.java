package me.mrbast.structory.crafting.listener;

import me.mrbast.structory.Structory;
import me.mrbast.structory.oldblockdata.newer.CustomBlockData;
import me.mrbast.structory.config.MainConfig;
import me.mrbast.structory.crafting.event.CraftingInteractStructureEvent;
import me.mrbast.structory.crafting.event.ItemInsertRecipeStructureEvent;
import me.mrbast.structory.crafting.event.RecipeSlotPickUpItemStructureEvent;
import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.crafting.recipe.RecipeSlot;
import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.event.PlayerInteractStructureEvent;
import me.mrbast.structory.manager.ListenerManager;
import me.mrbast.structory.manager.StructureInstanceManager;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.util.LogicUtil;
import me.mrbast.structory.util.SchedulerUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CraftingInteractionListener implements Listener {

    private Structory PLUGIN;
    private CraftingOption craftingOption;

    public CraftingInteractionListener(CraftingOption craftingOption) {
        this.craftingOption = craftingOption;
        this.PLUGIN = Structory.getPlugin(Structory.class);
    }

    /**
     * Recipe Slot interact event
     * @param event
     */
    @EventHandler
    public void onRecipeSlotInteract(PlayerInteractEvent event){

        if(!Version.getInstance().isMainHand(event)) return;
        if(event.getPlayer().isSneaking()) return;

        Block block = event.getClickedBlock();
        if(block == null) return;

        //USE CUSTOMBLOCKDATA
        CustomBlockData blockData = new CustomBlockData(block, PLUGIN);
        if(!blockData.has(StructureSpacedKey.RECIPE_SLOT.getNamespacedKey()))return;
        if(!blockData.has(StructureSpacedKey.RECIPE_SLOT_ID.getNamespacedKey())) return;

        String instanceUUID = blockData.get(StructureSpacedKey.RECIPE_SLOT.getNamespacedKey(), PersistentDataType.STRING);
        if( instanceUUID == null || instanceUUID.isEmpty()) return;
        UUID uuid;
        try {
            uuid = UUID.fromString(instanceUUID);
        }catch (IllegalArgumentException e){
            return;
        }

        Integer slot = blockData.get(StructureSpacedKey.RECIPE_SLOT_ID.getNamespacedKey(), PersistentDataType.INTEGER);
        if( slot == null )return;

        StructureInstanceManager.getInstance().get(uuid).ifPresent(instance -> {

            craftingOption.getCrafting(instance).ifPresent(crafting -> {

                if(crafting.isBusy()) return;

                if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    CraftingInteractStructureEvent PlayerInteractStructureEvent = new CraftingInteractStructureEvent(instance, event.getPlayer(), crafting);
                    ListenerManager.getInstance().call(PlayerInteractStructureEvent);
                    if(PlayerInteractStructureEvent.isCancelled()) return;

                    SchedulerUtil.entity(event.getPlayer(), ()->{
                        ItemStack toSet = event.getItem();
                        if(toSet == null) {
                            ItemStack took = crafting.take(slot);
                            if(took == null)return;

                            CraftingOption.getInstance().getCraftingData(instance.getData().getStructure()).ifPresent(craftingData->{
                                craftingData.take(instance.getData().getCenter().clone().add(0.0, 1, 0.0));
                            });
                            event.getPlayer().getInventory().addItem(took);

                            return;
                        }
                        toSet = toSet.clone();
                        toSet.setAmount(1);

                        if(event.getPlayer().getGameMode() != GameMode.CREATIVE) event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getItem().getAmount()-1);
                        ItemStack itemStack = crafting.replace(slot, toSet);
                        CraftingOption.getInstance().getCraftingData(instance.getData().getStructure()).ifPresent(craftingData->{
                            craftingData.place(instance.getData().getCenter().clone().add(0.0, 1, 0.0));
                        });
                        if(itemStack == null) return;


                        event.getPlayer().getInventory().addItem(itemStack);
                    });


                    event.setCancelled(true);



                }
            });





        });

    }

    /**
     * Dropping item into recipe
     * @param event
     */

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){

        Collection<StructureInstance> instances = StructureInstanceManager.getInstance().getFiltered(struct->struct.getData().getCenter().getWorld().equals(event.getItemDrop().getLocation().getWorld()) && struct.getData().getCenter().clone().add(0.5, 0, 0.5).distance(event.getItemDrop().getLocation()) <= 2 );
        if(instances.isEmpty()) return;


        Optional<StructureInstance> sampleInst = instances.stream().findFirst();
        sampleInst.ifPresent(instance->{

            craftingOption.getCrafting(instance).ifPresent(crafting -> {
                if(crafting.isBusy()) return;
                Item itemDrop = event.getItemDrop();

                ItemStack item = itemDrop.getItemStack();

                ItemInsertRecipeStructureEvent PlayerInteractStructureEvent = new ItemInsertRecipeStructureEvent(instance, event.getPlayer(), item);
                ListenerManager.getInstance().call(PlayerInteractStructureEvent);
                if(PlayerInteractStructureEvent.isCancelled()) return;


                if(!crafting.add(item)) return;


                itemDrop.remove();

                CraftingOption.getInstance().getCraftingData(instance.getData().getStructure()).ifPresent(craftingData->{
                    craftingData.insert(instance.getData().getCenter().clone().add(0.5, 1, 0.5));
                });
            });


        });

    }

    @EventHandler
    public void onRecipeSlotPickup(EntityPickupItemEvent e){


        if(isNotUsedInRecipeSlot(e.getItem().getItemStack())){ return;}

        e.setCancelled(true);

        if(!(e.getEntity() instanceof Player)) return;

        if( LogicUtil.xnor(!e.getEntity().isSneaking(),!MainConfig.shiftToTake ))  return;

        RecipeSlot.getRecipeSlot(e.getItem()).ifPresent(craft -> {
            if(e.getEntity()  instanceof Player){
                PlayerInteractStructureEvent PlayerInteractStructureEvent = new RecipeSlotPickUpItemStructureEvent(craft.getStructureInstance(), craft, (Player) e.getEntity());
                ListenerManager.getInstance().call(PlayerInteractStructureEvent);
                if(PlayerInteractStructureEvent.isCancelled()) return;
            }

            craft.remove();
            e.setCancelled(false);
        });




    }

    public boolean isNotUsedInRecipeSlot(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return !Objects.requireNonNull(meta).getPersistentDataContainer().has(StructureSpacedKey.RECIPE_SLOT_ITEM.getNamespacedKey(), PersistentDataType.STRING);
    }


    @EventHandler
    public void onRecipeSlotItemDeath(EntityDamageEvent e){


        if(!(e.getEntity() instanceof Item)) return;
        Item item = (Item) e.getEntity();
        if(isNotUsedInRecipeSlot(item.getItemStack())) return;

        e.getEntity().setVelocity(new Vector(0, 0,0));


        e.setCancelled(true);
    }


    @EventHandler
    public void onRecipeSlotItemDeSpawn(ItemDespawnEvent e){
        if(isNotUsedInRecipeSlot(e.getEntity().getItemStack())) return;

        e.setCancelled(true);
    }

}
