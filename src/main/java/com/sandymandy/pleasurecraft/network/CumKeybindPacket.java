package com.sandymandy.pleasurecraft.network;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CumKeybindPacket(Boolean pressed) implements CustomPayload {

    public static final Id<CumKeybindPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "cum_keybind_pressed"));

    public static final PacketCodec<RegistryByteBuf, CumKeybindPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.BOOLEAN, CumKeybindPacket::pressed,
                    CumKeybindPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
