package me.mrbast.structory.util;

import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class ItemStackUtil {

    // Serialize ItemStack to Base64 string
    public static String itemStackToBase64(ItemStack item) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(item);
        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    // Deserialize Base64 string to ItemStack
    public static ItemStack itemStackFromBase64(String base64) throws RuntimeException {
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            ObjectInputStream ois = new BukkitObjectInputStream(new ByteArrayInputStream(data));
            ItemStack item = (ItemStack) ois.readObject();
            ois.close();
            return item;
        } catch (Exception e) {
            throw new RuntimeException("Invalid item data", e);
        }
    }
}