package com.sandymandy.pleasurecraft;

import com.sandymandy.pleasurecraft.entity.EntityInit;
import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import com.sandymandy.pleasurecraft.item.ModItemGroups;
import com.sandymandy.pleasurecraft.item.ModItems;
import com.sandymandy.pleasurecraft.network.CumKeybindPacket;
import com.sandymandy.pleasurecraft.network.ThrustKeybindPacket;
import com.sandymandy.pleasurecraft.network.girls.AnimationSyncPacket;
import com.sandymandy.pleasurecraft.network.girls.BonePosSyncPacket;
import com.sandymandy.pleasurecraft.screen.GirlInventoryScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
		EntityInit.init();
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		registerKeybind();
	}

	private void registerKeybind(){
		PayloadTypeRegistry.playC2S().register(CumKeybindPacket.ID, CumKeybindPacket.CODEC);

		PayloadTypeRegistry.playC2S().register(ThrustKeybindPacket.ID, ThrustKeybindPacket.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(CumKeybindPacket.ID,
				(packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
							var entity = context.player().getVehicle();
							if (entity instanceof AbstractGirlEntity girl) {

								if(packet.pressed()){
									girl.getSceneManager().tryTriggerCum(context.player());
								}

							}

						}
				));

		ServerPlayNetworking.registerGlobalReceiver(ThrustKeybindPacket.ID,
				(packet, context) -> Objects.requireNonNull(context.player().getServer()).execute(() -> {
							var entity = context.player().getVehicle();
							if (entity instanceof AbstractGirlEntity girl) {
								girl.getSceneManager().setKeyHeld(packet.held());

							}

						}
				));
	}


	public record GirlScreenData(int entityId) {
		public static final PacketCodec<RegistryByteBuf, GirlScreenData> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.VAR_INT,
				GirlScreenData::entityId,
				GirlScreenData::new
		);
	}
}