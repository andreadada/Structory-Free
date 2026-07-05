package me.mrbast.structory;


import me.mrbast.dadagui.bukkit.BukkitGuiManager;
import me.mrbast.platform.Platform;
import me.mrbast.structory.async.StructureParticleScheduler;
import me.mrbast.structory.command.StructoryCommand;
import me.mrbast.structory.config.*;
import me.mrbast.structory.crafting.option.CraftingOption;
import me.mrbast.structory.listener.AltarGenericInteractionListener;
import me.mrbast.structory.listener.AltarGriefListener;
import me.mrbast.structory.listener.AltarInteractionListener;
import me.mrbast.structory.manager.*;
import me.mrbast.structory.metrics.Metrics;
import me.mrbast.structory.oldblockdata.newer.CustomBlockData;
import me.mrbast.structory.structure.builder.Builder;
//import me.mrbast.charms.util.Debugger;
import me.mrbast.structory.util.SchedulerUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public class Structory extends JavaPlugin {

    private Metrics metrics;
    private Platform platform;
    private final Logger LOGGER = this.getLogger();
    private BukkitGuiManager guiManager;

    private StructoryCommand structoryCommand;

    @Override
    public void onEnable() {


        SchedulerUtil.init();

        ConfigInit.init();
        Version.prepare(this);
        platform = Platform.prepare(this);

        guiManager = new BukkitGuiManager(this);
        guiManager.register();

        Builder.init();





        CustomBlockData.registerListener(this);

        ConfigManager.getInstance().load();

        OptionManager.getInstance().init();

        StructureParticleScheduler.getInstance().start();




        Bukkit.getServer().getPluginManager().registerEvents(new AltarInteractionListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AltarGenericInteractionListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AltarGriefListener(), this);

        if(MainConfig.getInstance().metrics()) metrics = new Metrics(this, 23235);
        LOGGER.info("Metrics: " + (metrics != null ? "enabled" : "disabled"));

        structoryCommand = new StructoryCommand();

        Objects.requireNonNull(getCommand("structory")).setExecutor(structoryCommand);
        Objects.requireNonNull(getCommand("structory")).setTabCompleter((commandSender, command, s, strings) -> structoryCommand.getArgumentTrie().tabComplete(structoryCommand.getArgumentTrie(), commandSender, command, s, strings));

        LOGGER.info("Plugin loaded!");

    }


    public StructoryCommand getStructoryCommand() {
        return structoryCommand;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (guiManager != null) {
            guiManager.unregister();
        }
        HandlerList.unregisterAll(this);

        CraftingOption.getInstance().dropAllRecipeItems();
        SchedulerUtil.shutdown();
        LOGGER.info("Plugin disabled ! :(");
        if (metrics != null) metrics.shutdown();

        RecipeManager.getInstance().clear();
        SavedItemManager.getInstance().clear();
        StructureManager.getInstance().clear();
        StructureInstanceManager.getInstance().clear();

        MessageConfig.getInstance().reload();
    }

    public Platform getPlatform() {
        return platform;
    }

    public BukkitGuiManager getGuiManager() {
        return guiManager;
    }
}
