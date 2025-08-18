package com.sandymandy.pleasurecraft.item;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PleasureCraftItemGroups {
    public static final ItemGroup PLEASURE_CRAFT_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(PleasureCraft.MOD_ID, "pleasure_craft_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(PleasureCraftItems.LUCY_SPAWN_EGG))
                    .displayName(Text.translatable("itemgroup.pleasurecraft.pleasure_craft_items"))
                    .entries((displayContext, entries) -> {
                      entries.add(PleasureCraftItems.LUCY_SPAWN_EGG);
                      entries.add(PleasureCraftItems.BIA_SPAWN_EGG);




                    }).build());

    /*public static final ItemGroup PLEASURE_CRAFT_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(PleasureCraft.MOD_ID, "pleasure_craft_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack("a block"))
                    .displayName(Text.translatable("itemgroup.pleasurecraft.pleasure_craft_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add();




                    }).build()); */


    public static void registerItemGroups(){
        PleasureCraft.LOGGER.info("Registering Item Groups for " + PleasureCraft.MOD_ID);
    }
}
