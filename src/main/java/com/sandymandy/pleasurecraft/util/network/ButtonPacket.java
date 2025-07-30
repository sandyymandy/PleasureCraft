package com.sandymandy.pleasurecraft.util.network;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ButtonPacket(int entityId, String actionId) implements CustomPayload {

    public static final Id<ButtonPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "girl_button"));

    public static final PacketCodec<RegistryByteBuf, ButtonPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, ButtonPacket::entityId,
                    PacketCodecs.STRING, ButtonPacket::actionId,
                    ButtonPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
