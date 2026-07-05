package me.mrbast.structory.util;

import me.mrbast.structory.enums.StructureSpacedKey;
import me.mrbast.structory.version.Version;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class FireworkUtil {





    public static Firework prepareFirework(Location location, int power, boolean flicker, Color[] mainColor, Color[] fadeColor){

        Firework fw = (Firework) Objects.requireNonNull(location.getWorld()).spawnEntity(location.clone(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(0);



        fwm.addEffect(FireworkEffect.builder().
                withColor(mainColor[ThreadLocalRandom.current().nextInt(mainColor.length)])
                .flicker(flicker)
                .withFade(fadeColor[ThreadLocalRandom.current().nextInt(fadeColor.length)])
                .build());

        fw.setFireworkMeta(fwm);

        fw.getPersistentDataContainer().set(StructureSpacedKey.FIREWORK_CANCEL_DMG.getNamespacedKey(), Version.getInstance().persistentDataTypeBoolean(), true);
        return fw;

    }


}
