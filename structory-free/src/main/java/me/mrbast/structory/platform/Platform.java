package me.mrbast.structory.platform;

/*
import me.mrbast.platform.PaperPlatform;
import me.mrbast.structory.Structory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class Platform {


    private static Platform platform;
    public static Platform getInstance(){
        return platform;
    }
    public static void prepare(Plugin plugin){

        try {
            Class.forName("io.papermc.paper.math.BlockPosition");
            platform = new PaperPlatform();
        } catch (ClassNotFoundException e) {
            platform = new SpigotPlatform();
        }

        Structory.getPlugin(Structory.class).getLogger().info("Working with: " + platform.getLabel());
    }

    public abstract String getLabel();

    public abstract String format(String text);
    public abstract void sendMessage(Player player, String message);
    public abstract void sendMessage(CommandSender sender, String message);

    public abstract void sendActionbar(Player player, String message);

}


 */