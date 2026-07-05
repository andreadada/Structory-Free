package me.mrbast.structory.version;

import me.mrbast.structory.Structory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public abstract class Version {

    private static final Logger LOGGER = Structory.getPlugin(Structory.class).getLogger();
    private static Version instance;
    private static List<VersionInstancer> versions = new ArrayList<>();
    public static Version getInstance(){
        return instance;
    }
    static{
        versions.add(new VersionInstancer(Pattern.compile("1\\.(21.*|21|20.*|20|19.*|19)"), VersionLatest.class));
        versions.add(new VersionInstancer(Pattern.compile("1\\.(17.*|17|18.*|18)"), Version1718.class));
    }





    private static class VersionInstancer {

        private final Pattern version;
        private final Class<?> classPath;
        public VersionInstancer(Pattern version, Class<?> classPath){
            this.version = version;
            this.classPath = classPath;
        }

    }

    public static void prepare(Structory plugin) throws RuntimeException{

        String version = Bukkit.getServer().getBukkitVersion();

        Optional<VersionInstancer> opt = versions.stream().filter(ver -> ver.version.matcher(version).find()).findFirst();

        if(opt.isPresent()){
            LOGGER.info("Hooked to nms " + opt.get().classPath);
            try {
                instance = (Version) opt.get().classPath.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                instance = new VersionDefault();
                LOGGER.info("Hooked to default versioning, could probably break");
            }
        }else{
            instance = new VersionDefault();
            LOGGER.info("Hooked to default versioning, could probably break");
        }

    }

    public abstract List<String> getComments(ConfigurationSection configSection, @NotNull String path);
    public abstract List<String> getInlineComments(ConfigurationSection configSection, @NotNull String path);
    public abstract void setComments(ConfigurationSection configSection, @NotNull String path, List<String> list);
    public abstract void setInlineComments(ConfigurationSection configSection, @NotNull String path, @Nullable List<String> comments);

    public abstract boolean isClimbing(Player player);

    public abstract boolean isZero(Vector vector);

    public abstract PersistentDataType<Byte, Boolean> persistentDataTypeBoolean();
    public abstract  <Z, T> boolean hasFilteredData(ItemStack item, NamespacedKey key, PersistentDataType<Z,T> type, Predicate<T> predicate);
    public abstract <Z,T> void setData(ItemStack itemStack, String test, PersistentDataType<Z, T> type, T i);
    public abstract <Z, T> boolean hasData(ItemStack item, NamespacedKey data, PersistentDataType<Z,T> type);



    public abstract void setSkullBlock(Block block, String texture);
    public abstract boolean isMainHand(PlayerInteractEvent event);

    public abstract ItemStack getSkinnedSkull(String skin);

    public abstract void playSound(Player player, Sound sound, float volume, float pitch);
    public abstract void playSound(Location location, Sound sound, float volume, float pitch);

    public abstract ItemStack getItemInMainHand(Player player);


}