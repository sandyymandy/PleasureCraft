package com.sandymandy.pleasurecraft;

import com.sandymandy.pleasurecraft.datagen.PleasureCraftItemTagProvider;
import com.sandymandy.pleasurecraft.datagen.PleasureCraftModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PleasureCraftDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(PleasureCraftItemTagProvider::new);
		pack.addProvider(PleasureCraftModelProvider::new);

	}
}
