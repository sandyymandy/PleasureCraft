package com.sandymandy.pleasurecraft.networking.C2S;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record InventoryButtonC2SPacket(int entityId, String actionId) implements CustomPayload {

    public static final Id<InventoryButtonC2SPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "girl_inventory_button"));

    public static final PacketCodec<RegistryByteBuf, InventoryButtonC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, InventoryButtonC2SPacket::entityId,
                    PacketCodecs.STRING, InventoryButtonC2SPacket::actionId,
                    InventoryButtonC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
