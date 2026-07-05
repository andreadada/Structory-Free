package me.mrbast.structory.listener;

import me.mrbast.structory.Structory;
import me.mrbast.structory.enums.StructureSpacedKey;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.persistence.PersistentDataType;

public class AltarGenericInteractionListener implements Listener {

    private final static Structory PLUGIN = Structory.getPlugin(Structory.class);



    @EventHandler
    public void onFirework(EntityDamageByEntityEvent  event){

        if(!(event.getDamager() instanceof Firework)) return;
        if(!event.getDamager().getPersistentDataContainer().has(StructureSpacedKey.FIREWORK_CANCEL_DMG.getNamespacedKey(),  PersistentDataType.BOOLEAN)) return;
        event.setCancelled(true);


    }




    /**
     *
     * AltarInteractEvent event
     *
     * @param event
     */
    /*
    @EventHandler
    @SuppressWarnings(value = "unused")
    public void onInteract(PlayerInteractEvent event) {

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!Version.getInstance().isMainHand(event)) return;


        final Location clickedBlockLocation = Objects.requireNonNull(event.getClickedBlock()).getLocation();

        Collection<StructureInstance> val = StructureInstanceManager.getInstance().getFiltered(x -> x.getData().getMainBlockLocation().equals(clickedBlockLocation));
        Optional<StructureInstance> instance = val.stream().findAny();


        if(instance.isPresent()){



            event.setCancelled(true);


            PlayerInteractStructureEvent PlayerInteractStructureEvent = new PlayerInteractStructureEvent(instance.get(), event.getPlayer());
            Structure structure = instance.get().getData().getStructure();
            //if(!event.getPlayer().isSneaking() && (event.getItem() == null || event.getItem().getType() == Material.AIR)) PlayerInteractStructureEvent = new PlayerCraftStructureEvent(instance.get(), event.getPlayer());


            ListenerManager.getInstance().call(PlayerInteractStructureEvent);
            return;
        }




        if(!event.getPlayer().isSneaking()) return;

        event.setCancelled(true); //NON PIAZZO NULLA

        if(StructureValidator.checkNearby(clickedBlockLocation) != StructureValidator.Valid.VALID){
            AltarMessage.ERROR_ALTAR_NEARBY.send(event.getPlayer());
            return;
        }


        for(Structure structure : StructureManager.getInstance().getStructuresFiltered(x->
                MaterialUtil.areSame(x.getData().getCheckBlock(), clickedBlockLocation.getBlock().getType())))
        {

            StructureValidator structureValidator = getStructureValidator(event, structure, clickedBlockLocation);
            if(!structureValidator.isValid()) continue;

            event.setCancelled(true);
            return;
        }

    }

    private static @NotNull StructureValidator getStructureValidator(PlayerInteractEvent event, Structure structure, Location clickedBlockLocation) {
        StructureValidator structureValidator = new StructureValidator(structure, clickedBlockLocation);

        structureValidator.ifValid(instance-> {


            ListenerManager.getInstance().call(new AltarCreateEvent(event, instance));
            StructureInstanceManager.getInstance().register(instance);
            instance.init();

            SchedulerUtil.async(()->new SingleStructureInstanceConfig(instance).save());


        });
        return structureValidator;
    }

     */
}
