package com.sandymandy.pleasurecraft.scene;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.List;

public record SceneOption(
        String name,
        String introAnim,
        List<String> slowAnim,
        List<String> fastAnim,
        String cumAnim
) {
    public static final PacketCodec<RegistryByteBuf, SceneOption> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SceneOption::name,
            PacketCodecs.STRING, SceneOption::introAnim,
            PacketCodecs.collection(ArrayList::new , PacketCodecs.STRING), SceneOption::slowAnim,
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING), SceneOption::fastAnim,
            PacketCodecs.STRING, SceneOption::cumAnim,
            SceneOption::new
    );

}