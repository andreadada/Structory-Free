package me.mrbast.structory.config.parser;


import me.mrbast.dadaconfig.logic.ConfigSection;
import me.mrbast.dadaconfig.logic.ConfigurationParser;
import me.mrbast.dadaconfig.logic.Parameters;
import me.mrbast.structory.itembuilder.ItemBuilder;
import me.mrbast.structory.util.PersistentDataUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemBuilderConfigParser implements ConfigurationParser<ItemBuilder> {

    private Map<Material, SpecialItemBuilder> specialItemBuilders = new HashMap<>();
    public ItemBuilderConfigParser() {

        specialItemBuilders.put(Material.SPAWNER, (builder, section, path, parameters) -> {

            section.readString("type").ifPresent(type->{

                EntityType entityType;
                try{entityType = EntityType.valueOf(type.toUpperCase());}catch (IllegalArgumentException ignored){return;}


                builder.addAction(itemstack->{

                    ItemMeta meta = itemstack.getItemMeta();
                    if(meta == null) return;


                    if(meta instanceof BlockStateMeta){

                        BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                        BlockState state = blockStateMeta.getBlockState();
                        if(state instanceof CreatureSpawner){

                            CreatureSpawner creatureSpawner = (CreatureSpawner) state;
                            creatureSpawner.setSpawnedType(entityType);
                            blockStateMeta.setBlockState(creatureSpawner);
                            itemstack.setItemMeta(blockStateMeta);
                        }

                    }



                });



            });

        });

    }


    public static interface SpecialItemBuilder{


        void implement(ItemBuilder itemBuilder, ConfigSection section, String path, Parameters parameters);

    }

    @Override
    public Optional<ItemBuilder> read(ConfigSection section, String path, Parameters parameters) {


        Optional<Material> material_ = section.read(Material.class, "material");
        if(!material_.isPresent()) return Optional.empty();

        ItemBuilder itemBuilder = new ItemBuilder(material_.get());


        section.readString("displayName").ifPresent(itemBuilder::setName);

        if(section.contains("lore")){
            List<String> lore =  section.getStringList("lore");
            itemBuilder.setLore(lore);
        }

        section.readInt("amount").ifPresent(itemBuilder::setAmount);
        section.readBoolean("unbreakable").ifPresent(itemBuilder::setUnbreakable);

        section.getSection("enchantments").ifPresent(enchantments->{

            enchantments.getKeys(false).forEach(enchantment -> {
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantment.toLowerCase()));
                if (enchant == null) return;
                if(!enchantments.contains(enchantment)) return;

                int level = enchantments.getInt(enchantment);
                itemBuilder.addEnchantment(enchant, level);

            });


        });

        //section.readInt("model").ifPresent(itemBuilder::setModelData);

        section.getSection("set").ifPresent(setSection->{
            setSection.getNodes().forEach(keySection->{
                keySection.readString("type").ifPresent(type->{
                    keySection.read("value").ifPresent(value->{
                        PersistentDataType<Object, Object> dataType = PersistentDataUtil.getInstance().getDataType(type);
                        itemBuilder.setTag(keySection.getName(), dataType, value);
                    });
                });
            });
        });


        SpecialItemBuilder val = specialItemBuilders.get(material_.get());
        if(val != null) val.implement(itemBuilder, section, path, parameters);

        return Optional.of(itemBuilder);

    }

    @Override
    public void write(ConfigSection configuration, String path, ItemBuilder object) {

    }
}
