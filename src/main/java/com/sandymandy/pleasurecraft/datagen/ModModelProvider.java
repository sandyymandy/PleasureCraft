//package com.sandymandy.pleasurecraft.datagen;
//
//import com.sandymandy.pleasurecraft.item.ModItems;
//import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//import net.minecraft.client.data.BlockStateModelGenerator;
//import net.minecraft.client.data.ItemModelGenerator;
//import net.minecraft.client.data.Model;
//import net.minecraft.util.Identifier;
//
//import java.util.Optional;
//
//public class ModModelProvider extends FabricModelProvider {
//    public ModModelProvider(FabricDataOutput output) {
//        super(output);
//    }
//
//    @Override
//    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//
//    }
//
//    @Override
//    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
//        itemModelGenerator.register(ModItems.BIA_SPAWN_EGG, new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));
//        itemModelGenerator.register(ModItems.LUCY_SPAWN_EGG, new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));
//
//    }
//}
