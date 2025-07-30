//package com.sandymandy.pleasurecraft.datagen;
//
//import com.sandymandy.pleasurecraft.item.ModItems;
//import com.sandymandy.pleasurecraft.util.ModTags;
//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
//import net.minecraft.registry.RegistryWrapper;
//
//import java.util.concurrent.CompletableFuture;
//
//public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
//    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
//        super(output, completableFuture);
//    }
//
//    @Override
//    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
//        getOrCreateTagBuilder(ModTags.Items.TRANSFORMABLE_ITEMS)
//                .add(ModItems.LUCY_SPAWN_EGG)
//                .add(ModItems.BIA_SPAWN_EGG);
//    }
//}
