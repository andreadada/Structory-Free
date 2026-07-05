package me.mrbast.structory.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ArgumentTrieTabCompleter {


    @Nullable List<String> onTabComplete(ArgumentTrie trie, @NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings);
}
