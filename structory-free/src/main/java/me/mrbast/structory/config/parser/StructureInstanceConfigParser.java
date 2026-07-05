package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.dadaconfig.logic.Wrapper;
import me.mrbast.structory.Structory;
import me.mrbast.structory.event.LoadStructureInstance;
import me.mrbast.structory.manager.ListenerManager;
import me.mrbast.structory.manager.OptionManager;
import me.mrbast.structory.manager.StructureInstanceManager;
import me.mrbast.structory.manager.StructureManager;
import me.mrbast.structory.structure.Structure;
import me.mrbast.structory.structure.StructureInstance;
import me.mrbast.structory.structure.orientation.LevelOrientation;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.*;

public class StructureInstanceConfigParser implements ConfigurationParser<StructureInstance> {



    private final Map<String, Set<ConfigSection>> STRUCTURE_INSTANCE = new HashMap<>();

    public StructureInstanceConfigParser() {

        Structory.getPlugin(Structory.class).getServer().getPluginManager().registerEvents(new Listener() {



            @EventHandler
            public void onWorldLoad(WorldLoadEvent event) {

                final String worldName =  event.getWorld().getName();
                Set<ConfigSection> configs = STRUCTURE_INSTANCE.get(worldName);
                if (configs == null) return;
                if (configs.isEmpty()) return;
                STRUCTURE_INSTANCE.remove(worldName);

                Structory.getPlugin(Structory.class).getLogger().info("Loading queued instances in world " + worldName);


                configs.forEach(section -> read(section, "", Parameters.create()).ifPresent(structureInstance -> {

                    StructureInstanceManager.getInstance().register(structureInstance);
                    structureInstance.init();
                    ListenerManager.getInstance().call(new LoadStructureInstance(structureInstance));

                }));






            }



        }, Structory.getPlugin(Structory.class));

    }

    @Override
    public Optional<StructureInstance> read(ConfigSection section, String path, Parameters parameters) {


        Optional<String> structure_ = section.read(String.class, "structure");
        Optional<Structure> structure = structure_.map(structureName-> StructureManager.getInstance().getStructureByName(structureName));


        return structure.flatMap(value -> section.getSection("data").flatMap(dataSection -> dataSection.read(UUID.class, "uuid").map(uuid -> new StructureInstance(value, uuid)).map(structureInstance -> {



            Optional<Location> locationOpt = dataSection.getSection("center").flatMap(centerSection -> centerSection.read(Location.class, "" ));
            if(!locationOpt.isPresent()){

                dataSection.getSection("center").flatMap(centerSection -> centerSection.readString("world")).ifPresent(world -> {

                    STRUCTURE_INSTANCE.computeIfAbsent(world, x -> new HashSet<>()).add(section);
                    Structory.getPlugin(Structory.class).getLogger().severe("The world " + world + " is not yet loaded, adding to queue");

                });

                return null;

            }
            locationOpt.ifPresent(location -> structureInstance.getData().setCenter(location));
            dataSection.read(LevelOrientation.class, "orientation").ifPresent(orientation -> structureInstance.getData().setOrientation(orientation));

            OptionManager.getInstance().getHasInstanceData().forEach(dataOption -> dataOption.load(dataSection, path, structureInstance));

            return structureInstance;


        })));

        /*
        return structure
                .flatMap(value -> section.getSection("data").flatMap(dataSection -> //return structureInstance;
                dataSection.read(UUID.class, "uuid")
                        .map(uuid -> new StructureInstance(value, uuid))
                        .flatMap(structureInstance -> dataSection.getSection("center")
                                .flatMap(centerSection -> centerSection.read(Location.class, ""))
                                .map(location -> {

                                    structureInstance.getData().setCenter(location);
                                    dataSection.read(LevelOrientation.class, "orientation").ifPresent(orientation -> structureInstance.getData().setOrientation(orientation));

                                    OptionManager.getInstance().getHasInstanceData().forEach(dataOption -> dataOption.load(dataSection, path, structureInstance));

                                    return structureInstance;
                                })
                        )
                )
        );

         */
    }


    @Override
    public void write(ConfigSection section, String path, StructureInstance instance) {

        section.write("structure", instance.getData().getStructure().getData().getKey().getKey());
        section.createSection("data");
        section.getSection("data").ifPresent(dataSection->{


            dataSection.write("uuid", instance.getData().getUUID().toString());
            dataSection.createSection("center");
            dataSection.getSection("center").ifPresent(centerSection->{
                centerSection.write(Location.class, "", instance.getData().getCenter());
            });

            dataSection.write("orientation", instance.getData().getOrientation().toString());

            OptionManager.getInstance().getHasInstanceData().forEach(dataOption->{
                dataOption.save(dataSection, path, instance);
            });

        });



    }
}
