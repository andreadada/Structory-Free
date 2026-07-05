package me.mrbast.structory.message;

import me.mrbast.structory.config.MessageConfig;
import me.mrbast.structory.format.Format;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Message {

     void send(CommandSender sender);
     void send(Player player, Format format);

     void send(Player player);
     void send(Format f, Player player);
     void send(Format f, Player... player);
     void send(Format name, CommandSender sender);
}
