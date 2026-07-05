package me.mrbast.structory.version;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.mrbast.structory.Structory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class VersionLatest extends Version {


    @Override
    public <Z, T> boolean hasFilteredData(ItemStack item, NamespacedKey key, PersistentDataType<Z, T> type, Predicate<T> predicate) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        T value = container.get(key, type);

        return predicate.test(value);
    }

    @Override
    public <Z, T> boolean hasData(ItemStack item, NamespacedKey key, PersistentDataType<Z, T> type) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.has(key, type);
    }

    @Override
    public void setSkullBlock(Block block, String texture) {
        block.setType(Material.PLAYER_HEAD);
        Skull skull = (Skull) block.getState();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            Field field = skull.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skull, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skull.update(true);

    }
    @Override
    public boolean isMainHand(PlayerInteractEvent event) {
        return event.getHand() == EquipmentSlot.HAND;
    }

    @Override
    public ItemStack getSkinnedSkull(String skin) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", skin));
        Field profileField;
        try {
            profileField = Objects.requireNonNull(headMeta).getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {

        }
        head.setItemMeta(headMeta);
        return head;

    }

    @Override
    public void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player, sound, volume, pitch);
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    @Override
    public ItemStack getItemInMainHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public List<String> getComments(ConfigurationSection configSection, @NotNull String path) {
        return configSection.getComments(path);
    }

    @Override
    public List<String> getInlineComments(ConfigurationSection configSection, @NotNull String path) {
        return configSection.getInlineComments(path);
    }

    @Override
    public void setComments(ConfigurationSection configSection, @NotNull String path, List<String> list) {
        configSection.setComments(path, list);
    }

    @Override
    public void setInlineComments(ConfigurationSection configSection, @NotNull String path, @Nullable List<String> comments) {
        configSection.setInlineComments(path, comments);
    }

    @Override
    public boolean isClimbing(Player player) {
        return player.isClimbing();
    }

    @Override
    public PersistentDataType<Byte, Boolean> persistentDataTypeBoolean() {
        return PersistentDataType.BOOLEAN;
    }

    @Override
    public boolean isZero(Vector vector) {
        return vector.isZero();
    }

    @Override
    public <Z,T> void setData(ItemStack itemStack, String key, PersistentDataType<Z, T> type, T i) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(Structory.getPlugin(Structory.class), key), type, i);
        itemStack.setItemMeta(meta);
    }
}