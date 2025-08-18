package com.sandymandy.pleasurecraft.network.girls;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record ClothingArmorVisibilityS2CPacket(
        int entityId,
        List<Boolean> clothing,
        List<Boolean> armor
) implements CustomPayload {

    public static final Id<ClothingArmorVisibilityS2CPacket> ID =
            new Id<>(Identifier.of(PleasureCraft.MOD_ID, "sync_clothing_armor"));

    public static final PacketCodec<RegistryByteBuf, ClothingArmorVisibilityS2CPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, ClothingArmorVisibilityS2CPacket::entityId,
                    PacketCodecs.collection(ArrayList::new, PacketCodecs.BOOLEAN), ClothingArmorVisibilityS2CPacket::clothing,
                    PacketCodecs.collection(ArrayList::new, PacketCodecs.BOOLEAN), ClothingArmorVisibilityS2CPacket::armor,
                    ClothingArmorVisibilityS2CPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
