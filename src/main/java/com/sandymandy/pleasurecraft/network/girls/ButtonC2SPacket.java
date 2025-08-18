package com.sandymandy.pleasurecraft.network.girls;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ButtonC2SPacket(int entityId, String actionId) implements CustomPayload {

    public static final Id<ButtonC2SPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "girl_button"));

    public static final PacketCodec<RegistryByteBuf, ButtonC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, ButtonC2SPacket::entityId,
                    PacketCodecs.STRING, ButtonC2SPacket::actionId,
                    ButtonC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
