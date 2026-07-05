package me.mrbast.structory.listener;

import me.mrbast.structory.enums.StructureMessage;
import me.mrbast.structory.event.AltarCreateEvent;
import me.mrbast.structory.event.PlayerInteractStructureEvent;
import me.mrbast.structory.interaction.Interaction;
import me.mrbast.structory.manager.ListenerManager;
import me.mrbast.structory.manager.StructureInstanceManager;
import me.mrbast.structory.manager.StructureManager;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.structure.validator.StructureValidator;
import me.mrbast.structory.util.MaterialUtil;
import me.mrbast.structory.util.SchedulerUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class AltarInteractionListener implements Listener {


    /**
     *
     * AltarInteractEvent event
     *
     * @param event
     */
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

            PlayerInteractStructureEvent altarInteractionListener = new PlayerInteractStructureEvent(instance.get(), event.getPlayer());
            ListenerManager.getInstance().call(altarInteractionListener);
            if(altarInteractionListener.isCancelled()) return;

            Structure structure = instance.get().getData().getStructure();
            Interaction interaction = structure.getInteraction();
            interaction.handleInteract(instance.get(),  event);


            //TODO: Questo diventa un API da ascoltare per altri plugin
            return;
        }


        /*
        There is no instance so we should create one
         */

        if(!event.getPlayer().isSneaking()) return;

        if(StructureValidator.checkNearby(clickedBlockLocation) != StructureValidator.Valid.VALID){
            StructureMessage.ERROR_ALTAR_NEARBY.send(event.getPlayer());
            return;
        }

        for(Structure structure : StructureManager.getInstance().getStructuresFiltered(x->
                MaterialUtil.areSame(x.getData().getCheckBlock(), clickedBlockLocation.getBlock().getType())))
        {

            StructureValidator structureValidator = new StructureValidator(structure, clickedBlockLocation);
            structureValidator.check();

            if(!structureValidator.isValid()) continue;
            event.setCancelled(true);

            /*
            structureValidator.execute(inst-> {
                ListenerManager.getInstance().call(new AltarCreateEvent(event, inst));
                StructureInstanceManager.getInstance().register(inst);
                inst.init();
                SchedulerUtil.async(()->new SingleStructureInstanceConfig(inst).save());
            });
             */
            structureValidator.executeDefault(event);


            return;
        }

    }

}
