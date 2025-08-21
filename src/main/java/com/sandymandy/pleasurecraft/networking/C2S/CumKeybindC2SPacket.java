package com.sandymandy.pleasurecraft.networking.C2S;

import com.sandymandy.pleasurecraft.PleasureCraft;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CumKeybindC2SPacket(Boolean pressed) implements CustomPayload {

    public static final Id<CumKeybindC2SPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "cum_keybind_pressed"));

    public static final PacketCodec<RegistryByteBuf, CumKeybindC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.BOOLEAN, CumKeybindC2SPacket::pressed,
                    CumKeybindC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
