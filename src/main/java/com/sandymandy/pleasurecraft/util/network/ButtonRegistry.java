package com.sandymandy.pleasurecraft.util.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.List;

public class ButtonRegistry {
    public static final List<ButtonAction> BUTTONS_LEFT = List.of(
            new ButtonAction(Text.literal("Strip"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "stripOrDressup"));
            }),

            new ButtonAction(Text.literal("Break Up"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "breakUp"));
            }),

            new ButtonAction(Text.literal("Set Base Here"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "setBase"));
            }),

            new ButtonAction(Text.literal("Go To Base"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "goToBase"));
            })
    );

    public static final List<ButtonAction> BUTTONS_RIGHT = List.of(
            new ButtonAction(Text.literal("Talk"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "talk"));
            }),

            new ButtonAction(Text.literal("Sit"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "sit"));
            }),

            new ButtonAction(Text.literal("Follow Me"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "follow"));
            })
    );
}