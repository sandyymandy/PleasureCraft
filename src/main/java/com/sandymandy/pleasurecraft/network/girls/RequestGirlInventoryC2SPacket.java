package com.sandymandy.pleasurecraft.network.girls;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestGirlInventoryC2SPacket(int entityId) implements CustomPayload {
    public static final Id<RequestGirlInventoryC2SPacket> ID =
            new Id<>(Identifier.of(PleasureCraft.MOD_ID, "request_girl_inventory"));

    public static final PacketCodec<RegistryByteBuf, RequestGirlInventoryC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, RequestGirlInventoryC2SPacket::entityId,
                    RequestGirlInventoryC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}