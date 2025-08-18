package com.sandymandy.pleasurecraft;

import com.sandymandy.pleasurecraft.entity.PleasureCraftEntities;
import com.sandymandy.pleasurecraft.item.PleasureCraftItemGroups;
import com.sandymandy.pleasurecraft.item.PleasureCraftItems;
import com.sandymandy.pleasurecraft.network.PleasureCraftPackets;
import com.sandymandy.pleasurecraft.screen.GirlInventoryScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PleasureCraft implements ModInitializer {
	public static final String MOD_ID = "pleasurecraft";
	public static final Logger LOGGER = LoggerFactory.getLogger("PleasureCraft");

	public static final ExtendedScreenHandlerType<GirlInventoryScreenHandler, GirlScreenData> GIRL_SCREEN_HANDLER =
			Registry.register(
					Registries.SCREEN_HANDLER,
					Identifier.of(PleasureCraft.MOD_ID, "lucy_screen"),
					new ExtendedScreenHandlerType<>(GirlInventoryScreenHandler::new, GirlScreenData.PACKET_CODEC)
			);


	@Override
	public void onInitialize() {
		PleasureCraftEntities.register();
		PleasureCraftItemGroups.registerItemGroups();
		PleasureCraftItems.registerModItems();
		PleasureCraftPackets.registerPackets();
		PleasureCraftPackets.registerC2SPackets();
	}

	public record GirlScreenData(int entityId) {
		public static final PacketCodec<RegistryByteBuf, GirlScreenData> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.VAR_INT,
				GirlScreenData::entityId,
				GirlScreenData::new
		);
	}
}