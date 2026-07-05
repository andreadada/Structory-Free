package me.mrbast.structory.command;

import me.mrbast.structory.Structory;
import me.mrbast.structory.config.SingleSavedItemConfig;
import me.mrbast.structory.enums.StructureMessage;
import me.mrbast.structory.format.Format;
import me.mrbast.structory.manager.SavedItemManager;
import me.mrbast.structory.saveditem.SavedItemProvider;
import me.mrbast.structory.util.SchedulerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StructoryCommandOld implements CommandExecutor {

    private static ArgumentTrieTabCompleter savedItem = new ArgumentTrieTabCompleter() {
        @Override
        public @Nullable List<String> onTabComplete(ArgumentTrie trie, @NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

            Set<String> args = trie.getArguments();

            if(strings == null || strings.length == 0) return  args.stream().toList();

            String arg = strings[0];

            return SavedItemManager.getInstance().getAll().stream().filter(x->x.getKey().getKey().startsWith(arg)).map(x->x.getKey().getKey()).toList();

        }
    };

    private ArgumentTrie argumentTrie;

    public StructoryCommandOld() {
        argumentTrie = new ArgumentTrie("");
        argumentTrie.setCommand(true);
        argumentTrie.setExecutor((s, cmd, label, args) -> {
            StructureMessage.MAIN_COMMAND.send(s);
            return true;
        });
        argumentTrie.setPermission(new Permission("structory.cmd.use"));

        registerItemTree();


    }



    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        argumentTrie.execute(s, cmd, label, args);

        return true;
    }

    public ArgumentTrie getArgumentTrie() {
        return argumentTrie;
    }


    public void registerArgument(ArgumentTrie argumentTrie){
        argumentTrie.registerArgument(argumentTrie);
    }


    private void registerItemTree(){


        ArgumentTrie reloadTrie = new ArgumentTrie("reload", ((sender, command, label, args) -> {


            Structory.getPlugin(Structory.class).onDisable();
            Structory.getPlugin(Structory.class).onEnable();

            StructureMessage.CMD_RELOAD.send(Format.of().as(), sender);

            return true;

        }));

        ArgumentTrie helpTrie = new ArgumentTrie("help", ((sender, command, label, args) -> {
            StructureMessage.HELP_MAIN.send(sender);
            return true;
        }));




        ArgumentTrie itemTrie = new ArgumentTrie("item", (sender, cmd, alias, strings)-> {
            StructureMessage.HELP_ITEM.send(sender);
            return true;
        });
        /*
        itemTrie.registerCommand(new ArgumentTrie("help", (sender, cmd, alias, strings)-> {
            StructureMessage.HELP_ITEM.send(sender);
            return true;
        }, savedItem), "structory.cmd.item.help");

         */

        itemTrie.registerCommand(new ArgumentTrie("save", (sender, cmd, alias, strings)-> {

            if(strings == null || strings.length == 0) return true;

            String itemName = strings[0];

            if(itemName.isEmpty()) itemName = UUID.randomUUID().toString();


            if(!(sender instanceof Player)) {
                StructureMessage.PLAYER_ONLY.send(sender);
                return true;
            }

            Player player = (Player) sender;

            if(SavedItemManager.getInstance().has(itemName)) {
                StructureMessage.CUSTOM_ITEM_ALREADY_EXISTS.send(Format.of("name").as(itemName), player);
                return true;
            }


            ItemStack itemStack =  player.getInventory().getItemInMainHand().clone();
            SavedItemProvider savedItem = SavedItemManager.getInstance().prepare(itemName, itemStack);
            SavedItemManager.getInstance().register(savedItem);
            StructureMessage.CUSTOM_ITEM_CREATED.send(Format.of("name").as(savedItem.getKey().getKey()), player);
            SchedulerUtil.async(()-> {
                SchedulerUtil.async(()->new SingleSavedItemConfig(savedItem).save());

            });
            return true;
        }, savedItem), "structory.cmd.item.save");
        itemTrie.registerCommand(new ArgumentTrie("get", (sender, cmd, alias, strings)-> {
            if(strings == null || strings.length == 0) return true;

            String itemName = strings[0];

            if(itemName.isEmpty()) itemName = UUID.randomUUID().toString();


            if(!SavedItemManager.getInstance().has(itemName)) {
                StructureMessage.CUSTOM_ITEM_DONT_EXISTS.send(Format.of("name").as(itemName),sender);
                return true;
            }

            SavedItemProvider provider = SavedItemManager.getInstance().get(itemName);

            Player player = (Player) sender;
            player.getInventory().addItem(provider.getItem());

            return true;
        }, savedItem), "structory.cmd.item.get");



        itemTrie.registerCommand(new ArgumentTrie("replace", (sender, cmd, alias, strings)-> {

            if(strings == null || strings.length == 0) return true;

            String itemName = strings[0];

            if(itemName.isEmpty()) {
                StructureMessage.CUSTOM_ITEM_DONT_EXISTS.send(Format.of("name").as(itemName), sender);
            }


            if(!(sender instanceof Player)) {
                StructureMessage.PLAYER_ONLY.send(sender);
                return true;
            }

            Player player = (Player) sender;

            if(!SavedItemManager.getInstance().has(itemName)) {
                StructureMessage.CUSTOM_ITEM_DONT_EXISTS.send(Format.of("name").as(itemName), player);
                return true;
            }


            ItemStack itemStack =  player.getInventory().getItemInMainHand().clone();;
            SavedItemManager.getInstance().replace(itemName, itemStack);
            StructureMessage.CUSTOM_ITEM_REPLACED.send(Format.of("name").as(itemName), player);
            return true;
        }, savedItem), "structory.cmd.item.replace");

        itemTrie.registerCommand(new ArgumentTrie("delete", (sender, cmd, alias, strings)-> {
            if(strings == null || strings.length == 0) return true;

            String itemName = strings[0];

            if(itemName.isEmpty()) {
                return true;
            }




            if(!SavedItemManager.getInstance().has(itemName)) {
                StructureMessage.CUSTOM_ITEM_DONT_EXISTS.send(Format.of("name").as(itemName), sender);
                return true;
            }

            SavedItemProvider provider = SavedItemManager.getInstance().get(itemName);

            SavedItemManager.getInstance().delete(provider);
            StructureMessage.CUSTOM_ITEM_DELETED.send(Format.of("name").as(provider.getKey().getKey()), sender);
            SchedulerUtil.async(()-> {
                SchedulerUtil.async(()->new SingleSavedItemConfig(provider).delete());
            });

            return true;
        }, savedItem), "structory.cmd.item.delete");





        argumentTrie.registerCommand(reloadTrie, "structory.cmd.reload");
        argumentTrie.registerCommand(helpTrie, "structory.cmd.help");
        argumentTrie.registerArgument(itemTrie);

    }
}