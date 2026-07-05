package me.mrbast.structory.command;

import me.mrbast.structory.enums.StructureMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ErrorCommandExecutor implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        StructureMessage.COMMAND_NOT_FOUND.send(commandSender);
        return true;
    }
}
