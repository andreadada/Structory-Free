package me.mrbast.structory.format;

import me.mrbast.platform.Platform;
import me.mrbast.structory.Structory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/***
 * Used to format string
 */
public  class Formatter{

    private static final Platform platform  = Structory.getPlugin(Structory.class).getPlatform();

    private final String message;

    public Formatter(String message) {
        this.message = message;
    }

    public void send(Format format, Player... players){

        String formatted = message;
        for(Map.Entry<String, String> pair : format.getValues().entrySet()){
            formatted = formatted.replace("%"+pair.getKey()+"%", pair.getValue());
        }

        for (Player player : players) {
            platform.sendMessage(player, formatted);
        }
    }
    public String format(Format format){
        String formatted = message;
        for(Map.Entry<String, String> pair : format.getValues().entrySet()){
            formatted = formatted.replace("%"+pair.getKey()+"%", pair.getValue());
        }

        return formatted;
    }

    public void send(CommandSender... senders) {
        for (CommandSender sender : senders) {
            platform.sendMessage(sender, message);
        }
    }

    public void send(Player... players){
        for (Player player : players) {
            platform.sendMessage(player, message);
        }
    }

    public String get(){
        return platform.format(message);
    }

    public String get(Format format){
        return platform.format(format(format));
    }

    public void send(Format name, CommandSender sender) {
        platform.sendMessage(sender, format(name));
    }
}