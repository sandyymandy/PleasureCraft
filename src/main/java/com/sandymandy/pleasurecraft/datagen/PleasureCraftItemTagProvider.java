package com.sandymandy.pleasurecraft.datagen;

import com.sandymandy.pleasurecraft.item.PleasureCraftItems;
import com.sandymandy.pleasurecraft.util.PleasureCraftTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class PleasureCraftItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public PleasureCraftItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(PleasureCraftTags.Items.TRANSFORMABLE_ITEMS)
                .add(PleasureCraftItems.LUCY_SPAWN_EGG)
                .add(PleasureCraftItems.BIA_SPAWN_EGG);
    }
}
