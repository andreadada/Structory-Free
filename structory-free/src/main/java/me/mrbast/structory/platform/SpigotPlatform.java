package me.mrbast.structory.platform;

/*
import me.mrbast.platform.Platform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotPlatform extends Platform {



    /*
    public static class CustomMiniMessageParser {

        private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("<(red|yellow|green|blue|aqua|black|white|gray|dark_red|dark_green|dark_blue|dark_aqua|dark_gray|dark_purple|gold)>");
        private static final Pattern RGB_COLOR_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");
        private static final Pattern FORMAT_PATTERN = Pattern.compile("<(bold|italic|underline|strikethrough|reset)>");

        public static String parse(String message) {
            message = parseLegacyColors(message);
            message = parseRgbColors(message);
            message = parseFormats(message);
            return message;
        }

        private static String parseLegacyColors(String message) {
            Matcher matcher = LEGACY_COLOR_PATTERN.matcher(message);
            while (matcher.find()) {
                String color = matcher.group(1).toUpperCase();
                ChatColor chatColor = ChatColor.valueOf(color);
                message = message.replace(matcher.group(), chatColor.toString());
            }
            return message;
        }

        private static String parseRgbColors(String message) {
            Matcher matcher = RGB_COLOR_PATTERN.matcher(message);
            while (matcher.find()) {
                String hexColor = matcher.group(1);
                ChatColor chatColor;
                try {
                    chatColor = ChatColor.of("#" + hexColor);
                    message = message.replace(matcher.group(), chatColor.toString());
                } catch (NoSuchMethodError e) {

                }

            }
            return message;
        }


        private static String parseFormats(String message) {
            Matcher matcher = FORMAT_PATTERN.matcher(message);
            while (matcher.find()) {
                String format = matcher.group(1).toUpperCase();
                ChatColor chatFormat = ChatColor.valueOf(format);
                message = message.replace(matcher.group(), chatFormat.toString());
            }
            return message;
        }
    }

     */

/*
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.CharacterAndFormat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

private static final MiniMessage mm = MiniMessage.miniMessage();

    public SpigotPlatform(){

    }

    @Override
    public String format(String text) {
        return serializer().serialize(toComponent(text));
    }


    public Component toComponent(String text) {
        return mm.deserialize(text);
    }


    public String getLabel(){
        return "Spigot or Paper 1.16.5-";
    }


    @Override
    public void sendMessage(Player player, String message) {
        player.sendMessage(format(message));
    }

    @Override
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(format(message));
    }

    @Override
    public void sendActionbar(Player player, String message) {

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(format(message)));

    }


    public LegacyComponentSerializer serializer() {
        return LegacyComponentSerializer.builder()
                .character('§')
                .hexCharacter('#')
                .hexColors()
                .extractUrls()
                .useUnusualXRepeatedCharacterHexFormat()
                .flattener(ComponentFlattener.basic())
                .formats(CharacterAndFormat.defaults())
                .build();
    }




}


 */