package com.sandymandy.pleasurecraft.screen;

import com.sandymandy.pleasurecraft.network.girls.ButtonC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.List;

public class ButtonRegistry {
    public static final List<ButtonAction> BUTTONS_LEFT = List.of(
            new ButtonAction(Text.literal("Strip"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "stripOrDressup"));
            }),

            new ButtonAction(Text.literal("Break Up"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "breakUp"));
            }),

            new ButtonAction(Text.literal("Set Base Here"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "setBase"));
            }),

            new ButtonAction(Text.literal("Go To Base"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "goToBase"));
            }),

            new ButtonAction(Text.literal("Blow Job"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "blowjob"));
            })
    );

    public static final List<ButtonAction> BUTTONS_RIGHT = List.of(
            new ButtonAction(Text.literal("Talk"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "talk"));
            }),

            new ButtonAction(Text.literal("Tit Job"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "titjob"));
            }),

            new ButtonAction(Text.literal("Sit"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "sit"));
            }),

            new ButtonAction(Text.literal("Follow Me"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "follow"));
            }),

            new ButtonAction(Text.literal("Test Anim 1"), (girl, player) -> {
                ClientPlayNetworking.send(new ButtonC2SPacket(girl.getId(), "testAnim1"));
            })
    );
}