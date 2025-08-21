package com.sandymandy.pleasurecraft.networking.S2C;

import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.scene.SceneOption;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record SceneOptionsS2CPacket(int entityId, List<SceneOption> options) implements CustomPayload {
    public static final Id<SceneOptionsS2CPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "scene_options"));

    public static final PacketCodec<RegistryByteBuf, SceneOptionsS2CPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.VAR_INT, SceneOptionsS2CPacket::entityId,
                    PacketCodecs.collection(ArrayList::new, SceneOption.CODEC), SceneOptionsS2CPacket::options,
                    SceneOptionsS2CPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
