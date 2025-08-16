package com.sandymandy.pleasurecraft.network.girls;

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
            }),

            new ButtonAction(Text.literal("Test Anim 1"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "testAnim1"));
            })
    );

    public static final List<ButtonAction> BUTTONS_RIGHT = List.of(
            new ButtonAction(Text.literal("Talk"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "talk"));
            }),

            new ButtonAction(Text.literal("Sex"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "sex"));
            }),

            new ButtonAction(Text.literal("Sit"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "sit"));
            }),

            new ButtonAction(Text.literal("Follow Me"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "follow"));
            }),

            new ButtonAction(Text.literal("Test Anim 2"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonPacket(girl.getId(), "testAnim2"));
            })
    );
}