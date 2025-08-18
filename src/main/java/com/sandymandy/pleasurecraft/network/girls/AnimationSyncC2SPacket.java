package com.sandymandy.pleasurecraft.network.girls;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AnimationSyncC2SPacket(int entityId, String animationState, boolean loopState) implements CustomPayload {

    public static final Id<AnimationSyncC2SPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "sync_override_animation"));

    public static final PacketCodec<RegistryByteBuf, AnimationSyncC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, AnimationSyncC2SPacket::entityId,
                    PacketCodecs.STRING, AnimationSyncC2SPacket::animationState,
                    PacketCodecs.BOOLEAN, AnimationSyncC2SPacket::loopState,
                    AnimationSyncC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
