package com.sandymandy.pleasurecraft.item;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.EntityInit;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;


public class ModItems {


    public static final Item LUCY_SPAWN_EGG = registerItem("lucy_spawn_egg",
            setting -> new SpawnEggItem(EntityInit.LUCY , setting));

    public static final Item BIA_SPAWN_EGG = registerItem("bia_spawn_egg",
            setting -> new SpawnEggItem(EntityInit.BIA, setting));


    private static Item registerItem(String name, Function<Item.Settings, Item> factory) {
        Identifier id = Identifier.of(PleasureCraft.MOD_ID, name);
        return Registry.register(Registries.ITEM, id,
                factory.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
    }


    public static void registerModItems(){
        PleasureCraft.LOGGER.info("Registering Mod Items for " + PleasureCraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(LUCY_SPAWN_EGG);
            entries.add(BIA_SPAWN_EGG);
        });
    }
}
