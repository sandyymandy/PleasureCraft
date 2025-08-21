package com.sandymandy.pleasurecraft.networking.C2S;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MovementLockStateC2SPacket(int entityId, boolean data ) implements CustomPayload {
    public static final Id<MovementLockStateC2SPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "scene_options"));

    public static final PacketCodec<RegistryByteBuf, MovementLockStateC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, MovementLockStateC2SPacket::entityId,
                    PacketCodecs.BOOLEAN, MovementLockStateC2SPacket::data,
                    MovementLockStateC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
