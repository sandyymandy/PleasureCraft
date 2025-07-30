package com.sandymandy.pleasurecraft.util;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import com.sandymandy.pleasurecraft.PleasureCraft;

import java.util.UUID;

public record BonePosSyncPacket(int entityId, Vec3d position) implements CustomPayload {
    public static final Id<BonePosSyncPacket> ID = new Id<>(Identifier.of(PleasureCraft.MOD_ID, "sync_passenger_bone_pos"));


    // Manual Vec3d codec
    public static final PacketCodec<RegistryByteBuf, Vec3d> VEC3D_CODEC = PacketCodec.of(
            (vec,buf) -> {
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            },
            buf -> new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
    );


    // Tuple codec for the full payload
    public static final PacketCodec<RegistryByteBuf, BonePosSyncPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, BonePosSyncPacket::entityId,
            VEC3D_CODEC, BonePosSyncPacket::position,
            BonePosSyncPacket::new
    );
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
