
/*package me.mrbast.structory.manager;


import me.mrbast.structory.format.Format;
import me.mrbast.structory.saveditem.SavedItemProvider;
import org.bukkit.entity.Player; /**
 *


import me.mrbast.platform.Platform;
import me.mrbast.structory.Structory;
import me.mrbast.structory.config.SingleSavedItemConfig;
import me.mrbast.structory.config.SingleStructureInstanceConfig;
import me.mrbast.structory.enums.AltarMessage;
import me.mrbast.structory.format.Format;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.util.ItemStackUtil;
import me.mrbast.structory.util.SchedulerUtil;
import me.mrbast.structory.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.BukkitCommandContextKeys;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import org.jetbrains.annotations.NotNull;
import oshi.util.FormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.incendo.cloud.parser.standard.StringParser.stringParser;

public class CommandManager {

    private static final CommandManager INSTANCE = new CommandManager();
    public static CommandManager getInstance() {return INSTANCE; }

    public static class SavedItemParser<C> implements ArgumentParser<C, SavedItemProvider>, BlockingSuggestionProvider<C> {

        @Override
        public @NonNull ArgumentParseResult<SavedItemProvider> parse(@NotNull CommandContext<@NonNull C> commandContext, @NotNull CommandInput commandInput) {
            final String input = commandInput.peekString();
            SavedItemProvider savedItem = SavedItemManager.getInstance().get(input);
            if(savedItem  == null) return ArgumentParseResult.failure(new RuntimeException("Saved item '"+input+"' not found"));

            commandInput.readString();
            return ArgumentParseResult.success(savedItem);
        }

        public @NonNull Iterable<@NonNull Suggestion> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
            return SavedItemManager.getInstance().getAll().stream().map(x->x.getKey().getKey()).map(Suggestion::suggestion).toList();
        }

        public static <C> @NonNull ParserDescriptor<C, SavedItemProvider> savedItemParser() {
            return ParserDescriptor.of(new SavedItemParser<>(), SavedItemProvider.class);
        }
    }

    public void prepare(JavaPlugin plugin){


        LegacyPaperCommandManager<CommandSender> manager = LegacyPaperCommandManager.createNative(
                Structory.getPlugin(Structory.class),
                ExecutionCoordinator.simpleCoordinator()
        );


        manager.command(
                manager.commandBuilder("structory")
                        .permission(Permission.of("structory.cmd.main"))
                        .handler(context-> {

                        })
        );


        /*
        /structory item save <filename>
         */

/*


import me.mrbast.structory.format.Format;
import me.mrbast.structory.saveditem.SavedItemProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;manager.command(
                manager.commandBuilder("structory")
                        .permission(Permission.of("structory.cmd.item.save"))
                        .literal("item")
                        .literal("save")
                        .optional("name", stringParser())
                        .handler(context-> {

                            String itemName = context.getOrDefault("name", null);
                            if(itemName == null || itemName.isEmpty()) itemName = UUID.randomUUID().toString();


                            if(!(context.sender() instanceof Player)) {
                                AltarMessage.PLAYER_ONLY.send(context.sender());
                                return;
                            }

                            Player player = (Player) context.sender();

                            if(SavedItemManager.getInstance().has(itemName)) {
                                AltarMessage.CUSTOM_ITEM_ALREADY_EXISTS.send(Format.of("name").as(itemName), player);
                                return;
                            }


                            ItemStack itemStack =  player.getInventory().getItemInMainHand().clone();
                            SavedItemProvider savedItem = SavedItemManager.getInstance().prepare(itemName, itemStack);
                            SavedItemManager.getInstance().register(savedItem);
                            AltarMessage.CUSTOM_ITEM_CREATED.send(Format.of("name").as(savedItem.getKey().getKey()), player);
                            SchedulerUtil.async(()-> {
                                SchedulerUtil.async(()->new SingleSavedItemConfig(savedItem).save());

                            });
                        })
        );

        /*
        /structory item get <name>
         */

/*
import me.mrbast.structory.format.Format;
import me.mrbast.structory.saveditem.SavedItemProvider;
import org.bukkit.entity.Player;manager.command(
                manager.commandBuilder("structory")
                        .permission(Permission.of("structory.cmd.item.delete"))
                        .literal("item")
                        .literal("get")
                        .required("name", SavedItemParser.savedItemParser())
                        .handler(context-> {

                            SavedItemProvider provider = context.get("name");


                            /*
                            if(!SavedItemManager.getInstance().has(itemName)) {
                                AltarMessage.CUSTOM_ITEM_DONT_EXISTS.send(Format.of("name").as(itemName), context.sender());
                                return;
                            }

                             */
/*
                            Player player = (Player) context.sender();
                            player.getInventory().addItem(provider.getItem());
                        })
        );
        /*
        /structory item delete <name>
         */
/*
        manager.command(
                manager.commandBuilder("structory")
                        .permission(Permission.of("structory.cmd.item.delete"))
                        .literal("item")
                        .literal("delete")
                        .required("name", SavedItemParser.savedItemParser())
                        .handler(context-> {

                            SavedItemProvider provider = context.get("name");


                            /*
                            if(!SavedItemManager.getInstance().has(itemName)) {
                                AltarMessage.CUSTOM_ITEM_DONT_EXISTS.send(Format.of("name").as(itemName), context.sender());
                                return;
                            }

                             */
/*
                            SavedItemManager.getInstance().delete(provider);
                            AltarMessage.CUSTOM_ITEM_DELETED.send(Format.of("name").as(provider.getKey().getKey()), context.sender());
                            SchedulerUtil.async(()-> {
                                SchedulerUtil.async(()->new SingleSavedItemConfig(provider).delete());
                            });
                        })
        );
    }
}

*/