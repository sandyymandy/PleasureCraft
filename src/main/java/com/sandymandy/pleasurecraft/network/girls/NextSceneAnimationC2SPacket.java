package com.sandymandy.pleasurecraft.network.girls;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record NextSceneAnimationC2SPacket(int entityId, String finishedAnimation) implements CustomPayload {
    public static final Id<NextSceneAnimationC2SPacket> ID =
            new Id<>(Identifier.of(PleasureCraft.MOD_ID, "sync_scene_progress"));

    public static final PacketCodec<RegistryByteBuf, NextSceneAnimationC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, NextSceneAnimationC2SPacket::entityId,
                    PacketCodecs.STRING, NextSceneAnimationC2SPacket::finishedAnimation,
                    NextSceneAnimationC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}