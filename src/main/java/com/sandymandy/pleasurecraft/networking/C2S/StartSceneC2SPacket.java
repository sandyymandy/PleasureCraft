package com.sandymandy.pleasurecraft.networking.C2S;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record StartSceneC2SPacket(int entityId, String introAnim, List<String> slowAnim, List<String> fastAnim, String cumAnim) implements CustomPayload {

    public static final Id<StartSceneC2SPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "start_scene_from_client"));

    public static final PacketCodec<RegistryByteBuf, StartSceneC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, StartSceneC2SPacket::entityId,
                    PacketCodecs.STRING, StartSceneC2SPacket::introAnim,
                    PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING), StartSceneC2SPacket::slowAnim,
                    PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING), StartSceneC2SPacket::fastAnim,
                    PacketCodecs.STRING, StartSceneC2SPacket::cumAnim,
                    StartSceneC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
