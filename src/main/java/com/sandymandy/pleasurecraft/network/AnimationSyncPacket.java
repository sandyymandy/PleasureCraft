package com.sandymandy.pleasurecraft.network;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AnimationSyncPacket(int entityId, String animationState) implements CustomPayload {

    public static final Id<AnimationSyncPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "sync_override_animation"));

    public static final PacketCodec<RegistryByteBuf, AnimationSyncPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, AnimationSyncPacket::entityId,
                    PacketCodecs.STRING, AnimationSyncPacket::animationState,
                    AnimationSyncPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
