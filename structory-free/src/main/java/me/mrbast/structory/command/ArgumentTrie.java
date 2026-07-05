package me.mrbast.structory.command;

import me.mrbast.structory.enums.StructureMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;

import java.util.*;

public class ArgumentTrie {


    private String key;

    private Permission permission;
    private boolean command;
    private Map<String, ArgumentTrie> arguments;
    private CommandExecutor executor;
    private CommandExecutor error = new ErrorCommandExecutor();
    private ArgumentTrieTabCompleter tabCompleter = ((trie, commandSender, command, s, strings) -> {

        Set<String> args = trie.getArguments();

        if(strings == null || strings.length == 0) return  args.stream().toList();

        String arg = strings[0];

        return args.stream().filter(x->x.startsWith(arg)).toList();


    });

    public ArgumentTrie(String key) {
        arguments = new HashMap<>();
        setKey(key);
    }
    public ArgumentTrie(String key, CommandExecutor executor) {
        this(key);
        this.executor = executor;
    }
    public ArgumentTrie(String key, CommandExecutor executor, ArgumentTrieTabCompleter argumentTrieTabCompleter) {
        this(key, executor);
        this.tabCompleter = argumentTrieTabCompleter;
    }


    public List<String> tabComplete(ArgumentTrie argumentTrie, CommandSender sender, Command command, String alias, String[] args) {

        if(permission != null && !sender.hasPermission(permission)){
            return new ArrayList<>();
        }

        if(args == null || args.length == 1){
            return tabCompleter.onTabComplete(argumentTrie, sender, command, alias, args);
        }

        String arg0 = args[0];
        ArgumentTrie trie = arguments.get(arg0);
        if(trie == null){
            return new ArrayList<>();
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        return trie.tabComplete(trie, sender, command, alias, newArgs);
    }


    public void execute(CommandSender s, Command cmd, String label, String[] args){

        if(permission != null && !s.hasPermission(permission)){
            StructureMessage.NO_PEX.send(s);
            return;
        }

        if(isCommand()){
            executor.onCommand(s, cmd, label, args);
            return;
        }


        if(args == null || args.length == 0){
            if(executor != null) executor.onCommand(s, cmd, label, args);
            return;
        }

        String arg0 = args[0];


        ArgumentTrie trie = arguments.get(arg0);
        if(trie == null){
            error.onCommand(s, cmd, label, args);
            return;
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        trie.execute(s, cmd, label, newArgs);

    }

    public void setErrorExecutor(CommandExecutor e){
        error = e;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    public void registerArgument(ArgumentTrie value){
        arguments.put(value.getKey(), value);
    }
    public void registerCommand(ArgumentTrie value){
        value.setCommand(true);
        arguments.put(value.getKey(), value);
    }
    public void registerCommand(ArgumentTrie value, String permission){
        value.setCommand(true);
        value.setPermission(new Permission(permission));
        arguments.put(value.getKey(), value);
    }

    public ArgumentTrieTabCompleter getTabCompleter() {
        return tabCompleter;
    }

    public void setTabCompleter(ArgumentTrieTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    public Set<String> getArguments() {
        return arguments.keySet();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public boolean isCommand() {
        return command;
    }

    public void setCommand(boolean command) {
        this.command = command;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
