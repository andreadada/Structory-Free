package me.mrbast.structory.config;

import me.mrbast.dadaconfig.logic.Config;
import me.mrbast.structory.Structory;
import me.mrbast.structory.util.LoggerColor;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MainConfig extends Config {

    private static final MainConfig instance = new MainConfig();
    public static MainConfig getInstance() {
        return instance;
    }


    private long breakConfirmTime;
    public static boolean shiftToTake;
    public boolean disableItemPickup;
    public double distance;
    private boolean metrics;
    public boolean debug;


    private static final Logger LOGGER = Structory.getPlugin(Structory.class).getLogger();

    public MainConfig(){
        super();

    }

    public void updateFile() throws IOException, InvalidConfigurationException {
        if (this.contains("version")) {
            String currentVersion = this.getString("version");
            /*
            @Nullable InputStream resource = Structory.getPlugin(Structory.class).getResource("config.yml");


            if(resource != null) {


                @NotNull YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(resource));

             */
            if(!Structory.getPlugin(Structory.class).getDescription().getVersion().equalsIgnoreCase(currentVersion)) {



                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                String formattedDateTime = now.format(formatter);

                String fileName = "backup_" + formattedDateTime + ".yml";

                boolean ignored = configFile.renameTo(new File(Structory.getPlugin(Structory.class).getDataFolder(), "config_"+fileName));
                LOGGER.severe("New configuration found!");
                LOGGER.severe("Create backup for current config.yml ("+ LoggerColor.RED  + "config_"+fileName  + LoggerColor.RESET+")");
                LOGGER.severe("If something does not work as expected, create a backup for every file and check new formatting");
                this.init("config.yml", true);

            }
        }
    }

    @Override
    public void load() {

        try {
            init("config.yml", true);
            updateFile();
        } catch (IOException | InvalidConfigurationException e) {throw new RuntimeException(e);}



        this.getSection("metrics").flatMap(metrics -> metrics.readBoolean("enable")).ifPresent(enable -> this.metrics = enable);

        this.getSection("scheduler").ifPresent(scheduler->{
            scheduler.readInt("max_pool_size").ifPresent(maxPoolSize-> SchedulerUtil.getAsyncExecutor().setMaxPoolSize(maxPoolSize));
            scheduler.readInt("core_pool_size").ifPresent(corePoolSize-> SchedulerUtil.getAsyncExecutor().setCorePoolSize(corePoolSize));
            scheduler.readInt("keep_alive_time").ifPresent(keepAliveTime-> SchedulerUtil.getAsyncExecutor().setKeepAliveTime(keepAliveTime));
            scheduler.getSection("on_every").flatMap(onEvery -> onEvery.readInt("check")).ifPresent(check -> SchedulerUtil.getAsyncExecutor().setOnEvery(0, check, TimeUnit.MILLISECONDS));
        });


        this.getSection("structures").ifPresent(structures->{
            structures.read(Double.class, "distance").ifPresent(x -> distance = x);
            structures.read(Boolean.class, "shift_to_remove_item").ifPresent(x -> shiftToTake = !x);
            structures.read(Boolean.class, "disable_item_pickup").ifPresent(x -> disableItemPickup = x);
            structures.read(Integer.class, "break_confirm_time").ifPresent(x -> breakConfirmTime = x);
        });



        /*
        getSection("structures").ifPresentOrElse(structures-> {
            LOGGER.info("Loading structures...");
            structures.getNodes().forEach(structure ->{
                Optional<Structure> struct = structure.read(Structure.class, "");
                struct.ifPresent(str -> {
                    StructureManager.getInstance().registerStructure(str);
                });
            });
        }, ()->{
            LOGGER.warning("No structures loaded");
        });

         */




    }

    public long getBreakConfirmTime() {
        return breakConfirmTime;
    }

    public boolean metrics() {
        return metrics;
    }
}
