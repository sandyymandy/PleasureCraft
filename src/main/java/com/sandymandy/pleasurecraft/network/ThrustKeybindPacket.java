package com.sandymandy.pleasurecraft.network;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ThrustKeybindPacket(Boolean held) implements CustomPayload {

    public static final Id<ThrustKeybindPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "thrust_keybind_held"));

    public static final PacketCodec<RegistryByteBuf, ThrustKeybindPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.BOOLEAN, ThrustKeybindPacket::held,
                    ThrustKeybindPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}