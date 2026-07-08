package me.mrbast.structory.listener;

import me.mrbast.structory.config.MainConfig;
import me.mrbast.structory.crafting.Crafting;
import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.enums.StructureMessage;
import me.mrbast.structory.event.PlayerBlockBreakStructureEvent;
import me.mrbast.structory.event.PlayerBreakStructureEvent;
import me.mrbast.structory.event.PlayerInteractStructureEvent;
import me.mrbast.structory.event.StructureEvent;
import me.mrbast.structory.manager.ListenerManager;
import me.mrbast.structory.manager.StructureInstanceManager;
import me.mrbast.structory.message.Message;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.structure.grief.StructureProtection;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;



public class AltarGriefListener implements Listener {


    private final CraftingOption craftingOption;
    private final Map<StructureInstance, Long> repeatAltarBlockBreak = new ConcurrentHashMap<>();


    public AltarGriefListener() {
        this.craftingOption = CraftingOption.getInstance();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {


        Block block = event.getBlock();
        StructureInstance instance = StructureProtection.getInstance().get(block.getLocation());
        if(instance == null) return;


        ((Cancellable) event).setCancelled(true);




        if(!instance.getData().getMainBlockLocation().equals(block.getLocation())){
            PlayerInteractStructureEvent playerBreakStructureEvent = new PlayerBlockBreakStructureEvent(instance, event.getPlayer(),  block);
            ListenerManager.getInstance().call(playerBreakStructureEvent);
            if(playerBreakStructureEvent.isCancelled()) return;
            if(ThreadLocalRandom.current().nextDouble() > 0.4) StructureMessage.ERROR_BLOCK_DESTROY.send(event.getPlayer());
            return;
        }

        Optional<Crafting> crafting = craftingOption.getCrafting(instance);



        if(crafting.isPresent() && crafting.get().isBusy()) return;

        long now = System.currentTimeMillis();
        if(repeatAltarBlockBreak.containsKey(instance)){
            long before = repeatAltarBlockBreak.get(instance);

            if(now - before > 5000){
                repeatAltarBlockBreak.remove(instance);
                return;
            }

            PlayerInteractStructureEvent playerBreakStructureEvent = new PlayerBreakStructureEvent(event.getPlayer(), instance, block);
            ListenerManager.getInstance().call(playerBreakStructureEvent);
            if(playerBreakStructureEvent.isCancelled()) return;

            event.setCancelled(false);
            StructureInstanceManager.getInstance().delete(instance);
            crafting.ifPresent(Crafting::dropAll);

            return;
        }

        PlayerInteractStructureEvent playerBreakStructureEvent = new PlayerBlockBreakStructureEvent(instance, event.getPlayer(),  block);
        ListenerManager.getInstance().call(playerBreakStructureEvent);
        if(playerBreakStructureEvent.isCancelled()) return;



        repeatAltarBlockBreak.put(instance, now);
        StructureMessage.DESTROY_AGAIN_TO_BREAK.send(event.getPlayer());



    }


    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        handle(event);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        Block block = event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().clone().add(event.getDirection().getDirection()));
        StructureInstance instance = StructureProtection.getInstance().get(block.getLocation());
        if(instance == null) return;



        ((Cancellable) event).setCancelled(true);


    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {

        for(Block block : event.getBlocks()){

            BlockState old = block.getState();
            BlockData data = old.getBlockData();
            StructureInstance instance = StructureProtection.getInstance().get(block.getLocation());
            if(instance == null) continue;
            ((Cancellable) event).setCancelled(true);

            SchedulerUtil.regionLater(block.getLocation(), ()->{
                block.setBlockData(data);
                old.update();

            }, 1);



            return;
        }


    }

    @EventHandler
    public void onFade(BlockFadeEvent event) {
        handle(event);
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        handle(event);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        handle(event);
    }
    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        Block block = event.getSource();

        StructureInstance instance = StructureProtection.getInstance().get(block.getLocation());
        if(instance == null) return;

        ((Cancellable) event).setCancelled(true);
    }


    @EventHandler
    public void onFluid(FluidLevelChangeEvent event) {
        handle(event);
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        handle(event);
    }

    @EventHandler
    public void onCauldron(CauldronLevelChangeEvent event) {
        Block block = event.getBlock();

        StructureInstance instance = StructureProtection.getInstance().get(block.getLocation());
        if(instance == null) return;

        ((Cancellable) event).setCancelled(true);
    }



    @EventHandler
    public void onTnT(EntityExplodeEvent event){

        boolean matches = event.blockList().stream().anyMatch(x->{
            StructureInstance instance = StructureProtection.getInstance().get(x.getLocation());
            return instance != null;
        });

        if(!matches) return;


        event.setCancelled(true);

    }



    @EventHandler
    public void onExplode(BlockExplodeEvent event) {

        handle(event);
    }

    @EventHandler
    public void onDamage(BlockDamageEvent event) {
        handle(event);
    }

    private void handle(BlockEvent event){

        Block block = event.getBlock();

        StructureInstance instance = StructureProtection.getInstance().get(block.getLocation());
        if(instance == null) return;

        ((Cancellable) event).setCancelled(true);

        if(block.getType() == Material.PLAYER_HEAD){
            ((Cancellable) event).setCancelled(true);
            World world = event.getBlock().getWorld();
            int x,y,z;
            x = event.getBlock().getX();
            y = event.getBlock().getY();
            z = event.getBlock().getZ();
            Skull skull = (Skull) block.getState();
            BlockData data = skull.getBlockData();

            SchedulerUtil.regionLater(block.getLocation(), ()->{
                Block bl = world.getBlockAt(x,y,z);
                bl.setBlockData(data);
                bl.getState().update();

            }, 1);
        }


    }
}
